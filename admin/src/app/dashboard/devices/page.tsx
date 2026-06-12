'use client';
import { useQuery, useMutation } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { isOnline } from '@/types';
import type { Room } from '@/types';
import { RefreshCw, Power, Wifi, WifiOff } from 'lucide-react';
import { clsx } from 'clsx';

export default function DevicesPage() {
  const { data: devices = [], isLoading, refetch } = useQuery({
    queryKey: ['devices'],
    queryFn:  api.getDevices,
    refetchInterval: 30_000,
  });

  const rebootMutation  = useMutation({ mutationFn: api.rebootDevice });
  const refreshMutation = useMutation({ mutationFn: api.pushRefresh });

  const online  = devices.filter((d: Room) => isOnline(d.lastSeenAt)).length;
  const offline = devices.length - online;

  return (
    <div className="space-y-5">
      {/* Summary */}
      <div className="flex gap-4">
        <Pill icon={Wifi}    color="green" label={`${online} online`} />
        <Pill icon={WifiOff} color="red"   label={`${offline} offline`} />
      </div>

      {/* Table */}
      {isLoading ? (
        <Spinner />
      ) : (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 text-left">
                  <th className="px-5 py-3 font-medium text-slate-600">Room</th>
                  <th className="px-5 py-3 font-medium text-slate-600">Device Type</th>
                  <th className="px-5 py-3 font-medium text-slate-600">Last Ping</th>
                  <th className="px-5 py-3 font-medium text-slate-600">Status</th>
                  <th className="px-5 py-3 font-medium text-slate-600">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {devices
                  .sort((a: Room, b: Room) => a.roomNumber.localeCompare(b.roomNumber, undefined, { numeric: true }))
                  .map((device: Room) => {
                    const online = isOnline(device.lastSeenAt);
                    return (
                      <tr key={device.id} className={clsx('hover:bg-slate-50', !online && 'bg-red-50/40')}>
                        <td className="px-5 py-3.5 font-semibold text-slate-800">
                          Room {device.roomNumber}
                        </td>
                        <td className="px-5 py-3.5 text-slate-600 capitalize">
                          {device.deviceType.replace('_', ' ')}
                        </td>
                        <td className="px-5 py-3.5 text-slate-500">
                          {device.lastSeenAt
                            ? new Date(device.lastSeenAt).toLocaleString()
                            : <span className="text-slate-400">Never</span>}
                        </td>
                        <td className="px-5 py-3.5">
                          <span className={clsx(
                            'inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-xs font-medium',
                            online ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700',
                          )}>
                            <span className={clsx('w-1.5 h-1.5 rounded-full', online ? 'bg-emerald-500' : 'bg-red-500')} />
                            {online ? 'Online' : 'Offline'}
                          </span>
                        </td>
                        <td className="px-5 py-3.5">
                          <div className="flex items-center gap-2">
                            <ActionBtn
                              icon={RefreshCw}
                              label="Refresh"
                              loading={refreshMutation.isPending}
                              onClick={() => refreshMutation.mutate(device.id)}
                              color="blue"
                            />
                            <ActionBtn
                              icon={Power}
                              label="Reboot"
                              loading={rebootMutation.isPending}
                              onClick={() => rebootMutation.mutate(device.id)}
                              color="red"
                            />
                          </div>
                        </td>
                      </tr>
                    );
                  })}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

function Pill({ icon: Icon, color, label }: { icon: React.ElementType; color: 'green'|'red'; label: string }) {
  return (
    <div className={clsx(
      'flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium',
      color === 'green' ? 'bg-emerald-50 text-emerald-700' : 'bg-red-50 text-red-700',
    )}>
      <Icon size={15} />{label}
    </div>
  );
}

function ActionBtn({ icon: Icon, label, color, loading, onClick }: {
  icon: React.ElementType; label: string; color: 'blue'|'red'; loading: boolean; onClick: () => void;
}) {
  return (
    <button
      onClick={onClick}
      disabled={loading}
      title={label}
      className={clsx(
        'p-1.5 rounded transition-colors disabled:opacity-40',
        color === 'blue' ? 'text-slate-400 hover:text-blue-600 hover:bg-blue-50'
                        : 'text-slate-400 hover:text-red-600 hover:bg-red-50',
      )}
    >
      <Icon size={15} className={loading ? 'animate-spin' : ''} />
    </button>
  );
}

function Spinner() {
  return <div className="flex justify-center py-20"><div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" /></div>;
}
