import fp from 'fastify-plugin';
import fastifyStatic from '@fastify/static';
import { mkdir } from 'fs/promises';
import path from 'path';
import type { FastifyInstance } from 'fastify';

/**
 * Directory where uploaded media is stored. On Railway, attach a Volume and set
 * MEDIA_DIR to its mount path (e.g. /data/uploads). Defaults to ./uploads locally.
 */
export const MEDIA_DIR = path.resolve(process.env.MEDIA_DIR || 'uploads');

async function mediaPlugin(server: FastifyInstance) {
  await mkdir(MEDIA_DIR, { recursive: true });

  // Serve uploaded files at /media/<key>. Filenames are content-addressed
  // (random hex prefix) so they're safe to cache aggressively & immutably.
  await server.register(fastifyStatic, {
    root:          MEDIA_DIR,
    prefix:        '/media/',
    cacheControl:  true,
    maxAge:        '365d',
    immutable:     true,
  });
}

export default fp(mediaPlugin);
export { mediaPlugin };
