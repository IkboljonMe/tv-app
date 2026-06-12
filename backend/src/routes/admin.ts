/* Admin API — JWT-protected routes for hotel staff */
import { randomBytes } from 'crypto';
import bcrypt from 'bcryptjs';
import type { FastifyInstance, FastifyRequest } from 'fastify';
import { z } from 'zod';
import { pushToRoom, broadcastToHotel } from '../services/pushService';
import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';

/* ── Auth middleware ── */
async function requireAdmin(req: FastifyRequest): Promise<void> {
  await req.jwtVerify();
}

/* ── S3 client (lazy) ── */
let _s3: S3Client | null = null;
function getS3(): S3Client {
  if (!_s3) {
    _s3 = new S3Client({
      endpoint:  process.env.S3_ENDPOINT,
      region:    process.env.S3_REGION || 'us-east-1',
      credentials: {
        accessKeyId:     process.env.S3_ACCESS_KEY!,
        secretAccessKey: process.env.S3_SECRET_KEY!,
      },
      forcePathStyle: true,
    });
  }
  return _s3;
}

export async function adminRoutes(server: FastifyInstance) {
  /* ── POST /admin/login ──────────────────────────────────────────── */
  server.post('/admin/login', async (req, reply) => {
    const { email, password } = req.body as { email: string; password: string };
    if (!email || !password) return reply.status(400).send({ error: 'email + password required' });

    // Simple single-admin check via env (replace with DB users for multi-staff)
    const adminEmail = process.env.ADMIN_EMAIL;
    const adminHash  = process.env.ADMIN_PASSWORD_HASH;
    if (!adminEmail || !adminHash) return reply.status(503).send({ error: 'Admin not configured' });

    if (email !== adminEmail || !(await bcrypt.compare(password, adminHash))) {
      return reply.status(401).send({ error: 'Invalid credentials' });
    }

    const token = server.jwt.sign({ email, role: 'hotel_admin' }, { expiresIn: '8h' });
    return { token };
  });

  /* ── GET /admin/rooms ───────────────────────────────────────────── */
  server.get('/admin/rooms', { preHandler: requireAdmin }, async () => {
    return server.prisma.room.findMany({
      include: { guests: { orderBy: { createdAt: 'desc' }, take: 1 } },
      orderBy: { roomNumber: 'asc' },
    });
  });

  /* ── GET /admin/rooms/:id ───────────────────────────────────────── */
  server.get<{ Params: { id: string } }>(
    '/admin/rooms/:id',
    { preHandler: requireAdmin },
    async (req, reply) => {
      const room = await server.prisma.room.findUnique({
        where:   { id: req.params.id },
        include: { guests: true, hotel: true },
      });
      if (!room) return reply.status(404).send({ error: 'Not found' });
      return room;
    },
  );

  /* ── PUT /admin/rooms/:id/background ───────────────────────────── */
  server.put<{ Params: { id: string }; Body: { backgroundUrl: string } }>(
    '/admin/rooms/:id/background',
    { preHandler: requireAdmin },
    async (req) => {
      const room = await server.prisma.room.update({
        where: { id: req.params.id },
        data:  { backgroundUrl: req.body.backgroundUrl },
      });
      await pushToRoom(server, room.id, { type: 'UPDATE_BACKGROUND', url: req.body.backgroundUrl });
      return room;
    },
  );

  /* ── POST /admin/push/:roomId ───────────────────────────────────── */
  server.post<{ Params: { roomId: string }; Body: { message: string; duration?: number } }>(
    '/admin/push/:roomId',
    { preHandler: requireAdmin },
    async (req) => {
      await pushToRoom(server, req.params.roomId, {
        type:     'SHOW_ANNOUNCEMENT',
        message:  req.body.message,
        duration: req.body.duration ?? 15,
      });
      return { ok: true };
    },
  );

  /* ── GET /admin/services ────────────────────────────────────────── */
  server.get<{ Querystring: { hotel_id: string } }>(
    '/admin/services',
    { preHandler: requireAdmin },
    async (req) => {
      return server.prisma.hotelService.findMany({
        where:   { hotelId: req.query.hotel_id },
        orderBy: { sortOrder: 'asc' },
      });
    },
  );

  /* ── POST /admin/services ───────────────────────────────────────── */
  server.post(
    '/admin/services',
    { preHandler: requireAdmin },
    async (req, reply) => {
      const { hotelId, label, iconUrl, sortOrder, deepLink } = req.body as Record<string, unknown>;
      const svc = await server.prisma.hotelService.create({
        data: {
          hotelId:   hotelId as string,
          label:     label as object,
          iconUrl:   iconUrl as string | undefined,
          sortOrder: (sortOrder as number) ?? 0,
          deepLink:  deepLink as string | undefined,
        },
      });
      await broadcastToHotel(server, svc.hotelId, { type: 'REFRESH_CONFIG' });
      return reply.status(201).send(svc);
    },
  );

  /* ── PUT /admin/services/:id ────────────────────────────────────── */
  server.put<{ Params: { id: string } }>(
    '/admin/services/:id',
    { preHandler: requireAdmin },
    async (req) => {
      const { label, iconUrl, sortOrder, available, deepLink } = req.body as Record<string, unknown>;
      const svc = await server.prisma.hotelService.update({
        where: { id: req.params.id },
        data:  { label: label as object, iconUrl: iconUrl as string, sortOrder: sortOrder as number, available: available as boolean, deepLink: deepLink as string },
      });
      await broadcastToHotel(server, svc.hotelId, { type: 'REFRESH_CONFIG' });
      return svc;
    },
  );

  /* ── DELETE /admin/services/:id ─────────────────────────────────── */
  server.delete<{ Params: { id: string } }>(
    '/admin/services/:id',
    { preHandler: requireAdmin },
    async (req) => {
      const svc = await server.prisma.hotelService.delete({ where: { id: req.params.id } });
      await broadcastToHotel(server, svc.hotelId, { type: 'REFRESH_CONFIG' });
      return { ok: true };
    },
  );

  /* ── GET /admin/devices ─────────────────────────────────────────── */
  server.get('/admin/devices', { preHandler: requireAdmin }, async () => {
    const cutoff = new Date(Date.now() - 5 * 60_000); // 5 min ago
    const rooms = await server.prisma.room.findMany({
      orderBy: { roomNumber: 'asc' },
    });
    return rooms.map((r) => ({
      ...r,
      online: r.lastSeenAt ? r.lastSeenAt > cutoff : false,
    }));
  });

  /* ── POST /admin/push-reboot/:roomId ────────────────────────────── */
  server.post<{ Params: { roomId: string } }>(
    '/admin/push-reboot/:roomId',
    { preHandler: requireAdmin },
    async (req) => {
      await pushToRoom(server, req.params.roomId, { type: 'REBOOT' });
      return { ok: true };
    },
  );

  /* ── POST /admin/media/presign ──────────────────────────────────── */
  server.post<{ Body: { filename: string; contentType: string } }>(
    '/admin/media/presign',
    { preHandler: requireAdmin },
    async (req) => {
      const key  = `uploads/${randomBytes(16).toString('hex')}-${req.body.filename}`;
      const cmd  = new PutObjectCommand({
        Bucket:      process.env.S3_BUCKET,
        Key:         key,
        ContentType: req.body.contentType,
      });
      const url = await getSignedUrl(getS3(), cmd, { expiresIn: 3600 });
      const publicUrl = `${process.env.S3_ENDPOINT}/${process.env.S3_BUCKET}/${key}`;
      return { uploadUrl: url, publicUrl };
    },
  );

  /* ── GET /admin/content ─────────────────────────────────────────── */
  server.get<{ Querystring: { hotel_id: string } }>(
    '/admin/content',
    { preHandler: requireAdmin },
    async (req) => {
      return server.prisma.hotelContent.findMany({
        where:   { hotelId: req.query.hotel_id },
        orderBy: { createdAt: 'desc' },
      });
    },
  );

  /* ── POST /admin/content ────────────────────────────────────────── */
  server.post(
    '/admin/content',
    { preHandler: requireAdmin },
    async (req, reply) => {
      const b = req.body as Record<string, unknown>;
      const item = await server.prisma.hotelContent.create({
        data: {
          hotelId:     b.hotelId as string,
          contentType: b.contentType as never,
          title:       b.title as object | undefined,
          body:        b.body  as object | undefined,
          mediaUrl:    b.mediaUrl as string | undefined,
          active:      (b.active as boolean) ?? true,
          displayFrom:  b.displayFrom  ? new Date(b.displayFrom  as string) : undefined,
          displayUntil: b.displayUntil ? new Date(b.displayUntil as string) : undefined,
          priority:    b.priority as string | undefined,
        },
      });
      return reply.status(201).send(item);
    },
  );

  /* ── PUT /admin/content/:id ─────────────────────────────────────── */
  server.put<{ Params: { id: string } }>(
    '/admin/content/:id',
    { preHandler: requireAdmin },
    async (req) => {
      const b = req.body as Record<string, unknown>;
      return server.prisma.hotelContent.update({
        where: { id: req.params.id },
        data: {
          title:        b.title  as object | undefined,
          body:         b.body   as object | undefined,
          mediaUrl:     b.mediaUrl as string | undefined,
          active:       b.active  as boolean | undefined,
          displayFrom:  b.displayFrom  ? new Date(b.displayFrom  as string) : undefined,
          displayUntil: b.displayUntil ? new Date(b.displayUntil as string) : undefined,
          priority:     b.priority as string | undefined,
        },
      });
    },
  );
}
