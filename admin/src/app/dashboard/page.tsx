'use client';
import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { isOnline, getLabel } from '@/types';
import type { Room } from '@/types';
import { Hotel, Tv, WifiOff, Users } from 'lucide-react';
import Link from 'next/link';
import { clsx } from 'clsx';

/* ── Stat card ── */
function StatCard({ title, value, icon: Icon, color }: {
  title: string; value: number; icon: React.ElementType; color: string;
}) {
  return (
    <div className="bg-white rounded-xl p-5 border border-slate-200 flex items-center gap-4">
      <div className={clsx('w-12 h-12 rounded-xl flex items-center justify-center', color)}>
        <Icon size={22} className="text-white" />
      </div>
      <div>
        <p className="text-2xl font-bold text-slate-800">{value}</p>
        <p className="text-sm text-slate-500">{title}</p>
      </div>
    </div>
  );
}

/* ── Room card ── */
function RoomCard({ room }: { room: Room }) {
  const guest   = room.guests?.[0];
  const online  = isOnline(room.lastSeenAt);
  const occupied = !!guest;

  return (
    <Link href={`/dashboard/rooms/${room.id}`}>
      <div className={clsx(
        'border-2 rounded-xl p-3 cursor-pointer hover:shadow-md transition-all',
        !online          ? 'border-red-300 bg-red-50'
          : occupied     ? 'border-emerald-300 bg-emerald-50'
          :                'border-slate-200 bg-white',
      )}>
        <div className="flex items-center justify-between mb-1">
          <span className="font-bold text-slate-800 text-sm">{room.roomNumber}</span>
          <span className={clsx(
            'w-2 h-2 rounded-full',
            !online ? 'bg-red-500' : 'bg-emerald-500',
          )} />
        </div>
        {occupied ? (
          <p className="text-xs text-slate-600 truncate">
            {guest.guestFirstName} {guest.guestLastName}
          </p>
        ) : (
          <p className="text-xs text-slate-400">{online ? 'Vacant' : 'Offline'}</p>
        )}
        <p className="text-xs text-slate-400 mt-1 capitalize">
          {room.deviceType.replace('_', ' ')}
        </p>
      </div>
    </Link>
  );
}

export default function DashboardPage() {
  const { data: rooms = [], isLoading, error } = useQuery({
    queryKey: ['rooms'],
    queryFn: api.getRooms,
    refetchInterval: 30_000,
  });

  const occupied = rooms.filter(r => r.guests?.length > 0).length;
  const online   = rooms.filter(r => isOnline(r.lastSeenAt)).length;
  const offline  = rooms.length - online;

  // Group by floor
  const byFloor = rooms.reduce<Record<string, Room[]>>((acc, r) => {
    const key = r.floor != null ? `Floor ${r.floor}` : 'Unassigned';
    (acc[key] ??= []).push(r);
    return acc;
  }, {});

  if (isLoading) return <LoadingState />;
  if (error)     return <ErrorState message={(error as Error).message} />;

  return (
    <div className="space-y-6">
      {/* Stats */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard title="Total Rooms"  value={rooms.length} icon={Hotel}  color="bg-blue-500" />
        <StatCard title="Occupied"     value={occupied}     icon={Users}  color="bg-emerald-500" />
        <StatCard title="TVs Online"   value={online}       icon={Tv}     color="bg-violet-500" />
        <StatCard title="TVs Offline"  value={offline}      icon={WifiOff} color="bg-red-500" />
      </div>

      {/* Room grid */}
      <div className="bg-white rounded-xl border border-slate-200 p-5">
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-semibold text-slate-800">Room Occupancy</h2>
          <Legend />
        </div>

        {Object.keys(byFloor).length === 0 ? (
          <p className="text-slate-400 text-sm">No rooms configured yet.</p>
        ) : (
          Object.entries(byFloor).sort().map(([floor, floorRooms]) => (
            <div key={floor} className="mb-5">
              <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-2">{floor}</p>
              <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-7 lg:grid-cols-10 gap-2">
                {floorRooms
                  .sort((a, b) => a.roomNumber.localeCompare(b.roomNumber, undefined, { numeric: true }))
                  .map(r => <RoomCard key={r.id} room={r} />)}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

function Legend() {
  return (
    <div className="flex items-center gap-4 text-xs text-slate-500">
      <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded border-2 border-emerald-300 bg-emerald-50 inline-block"/> Occupied</span>
      <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded border-2 border-slate-200 bg-white inline-block"/> Vacant</span>
      <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded border-2 border-red-300 bg-red-50 inline-block"/> Offline</span>
    </div>
  );
}

function LoadingState() {
  return (
    <div className="flex items-center justify-center h-64">
      <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
    </div>
  );
}

function ErrorState({ message }: { message: string }) {
  return (
    <div className="bg-red-50 border border-red-200 rounded-xl p-6 text-red-700">{message}</div>
  );
}
