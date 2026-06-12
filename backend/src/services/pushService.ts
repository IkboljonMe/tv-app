/* Push real-time events to room TVs via Redis pub/sub */
import type { FastifyInstance } from 'fastify';

export type WsEventType =
  | 'REFRESH_CONFIG'
  | 'CLEAR_GUEST'
  | 'UPDATE_BACKGROUND'
  | 'SHOW_ANNOUNCEMENT'
  | 'REBOOT'
  | 'PING';

export interface WsEvent {
  type:      WsEventType;
  message?:  string;
  duration?: number;
  url?:      string;
}

/** Channel name for a single room */
export function roomChannel(roomId: string): string {
  return `room:${roomId}`;
}

/** Publish an event to a single room */
export async function pushToRoom(
  server: FastifyInstance,
  roomId: string,
  event: WsEvent,
): Promise<void> {
  await server.redis.publish(roomChannel(roomId), JSON.stringify(event));
}

/** Publish to every room that belongs to a hotel */
export async function broadcastToHotel(
  server: FastifyInstance,
  hotelId: string,
  event: WsEvent,
): Promise<void> {
  const rooms = await server.prisma.room.findMany({
    where: { hotelId },
    select: { id: true },
  });
  await Promise.all(rooms.map((r) => pushToRoom(server, r.id, event)));
}
