'use client';
import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { isOnline } from '@/types';
import type { Room } from '@/types';
import Link from 'next/link';
import { clsx } from 'clsx';
import { ChevronRight } from 'lucide-react';

function StatusBadge({ online, occupied }: { online: boolean; occupied: boolean }) {
  if (!online)   return <Badge color="red">Offline</Badge>;
  if (occupied)  return <Badge color="green">Occupied</Badge>;
  return <Badge color="gray">Vacant</Badge>;
}

function Badge({ color, children }: { color: 'red'|'green'|'gray'; children: React.ReactNode }) {
  return (
    <span className={clsx(
      'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium',
      color === 'red'   && 'bg-red-100 text-red-700',
      color === 'green' && 'bg-emerald-100 text-emerald-700',
      color === 'gray'  && 'bg-slate-100 text-slate-600',
    )}>{children}</span>
  );
}

export default function RoomsPage() {
  const { data: rooms = [], isLoading } = useQuery({
    queryKey: ['rooms'],
    queryFn: api.getRooms,
    refetchInterval: 30_000,
  });

  if (isLoading) return (
    <div className="flex justify-center py-20">
      <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
    </div>
  );

  return (
    <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
      <div className="px-5 py-4 border-b border-slate-200">
        <p className="text-sm text-slate-500">{rooms.length} rooms total</p>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="bg-slate-50 text-left">
              <th className="px-5 py-3 font-medium text-slate-600">Room</th>
              <th className="px-5 py-3 font-medium text-slate-600">Floor</th>
              <th className="px-5 py-3 font-medium text-slate-600">Guest</th>
              <th className="px-5 py-3 font-medium text-slate-600">Device</th>
              <th className="px-5 py-3 font-medium text-slate-600">Last Seen</th>
              <th className="px-5 py-3 font-medium text-slate-600">Status</th>
              <th className="px-5 py-3" />
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {rooms
              .sort((a, b) => a.roomNumber.localeCompare(b.roomNumber, undefined, { numeric: true }))
              .map((room: Room) => {
                const guest    = room.guests?.[0];
                const online   = isOnline(room.lastSeenAt);
                const occupied = !!guest;
                return (
                  <tr key={room.id} className="hover:bg-slate-50 transition-colors">
                    <td className="px-5 py-3.5 font-semibold text-slate-800">{room.roomNumber}</td>
                    <td className="px-5 py-3.5 text-slate-600">{room.floor ?? '—'}</td>
                    <td className="px-5 py-3.5 text-slate-700">
                      {guest ? `${guest.guestFirstName} ${guest.guestLastName}` : <span className="text-slate-400">—</span>}
                    </td>
                    <td className="px-5 py-3.5">
                      <span className="capitalize text-slate-600">{room.deviceType.replace('_', ' ')}</span>
                    </td>
                    <td className="px-5 py-3.5 text-slate-500">
                      {room.lastSeenAt
                        ? new Date(room.lastSeenAt).toLocaleTimeString()
                        : <span className="text-slate-400">Never</span>}
                    </td>
                    <td className="px-5 py-3.5">
                      <StatusBadge online={online} occupied={occupied} />
                    </td>
                    <td className="px-5 py-3.5">
                      <Link href={`/dashboard/rooms/${room.id}`}
                            className="text-blue-600 hover:text-blue-800 flex items-center gap-0.5 text-xs font-medium">
                        View <ChevronRight size={14} />
                      </Link>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>
    </div>
  );
}
