import { PrismaClient } from '@prisma/client';
import fp from 'fastify-plugin';
import type { FastifyInstance } from 'fastify';

declare module 'fastify' {
  interface FastifyInstance {
    prisma: PrismaClient;
  }
}

async function prismaPlugin(server: FastifyInstance) {
  const prisma = new PrismaClient({
    log: process.env.NODE_ENV === 'development' ? ['query', 'warn', 'error'] : ['error'],
  });
  await prisma.$connect();
  server.decorate('prisma', prisma);
  server.addHook('onClose', async () => { await prisma.$disconnect(); });
}

export default fp(prismaPlugin);
export { prismaPlugin };
