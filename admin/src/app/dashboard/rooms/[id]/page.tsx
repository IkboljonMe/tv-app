'use client';
import { use, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { isOnline } from '@/types';
import { ArrowLeft, RefreshCw, Megaphone, Image, Power } from 'lucide-react';
import Link from 'next/link';
import { clsx } from 'clsx';

export default function RoomDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const qc     = useQueryClient();

  const { data: room, isLoading } = useQuery({
    queryKey: ['room', id],
    queryFn: () => api.getRoom(id),
  });

  const [bgUrl,      setBgUrl]      = useState('');
  const [annoMsg,    setAnnoMsg]    = useState('');
  const [annoDur,    setAnnoDur]    = useState(15);
  const [feedback,   setFeedback]   = useState('');

  const bgMutation = useMutation({
    mutationFn: (url: string) => api.setRoomBackground(id, url),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['room', id] }); setFeedback('Background updated'); },
  });

  const annoMutation = useMutation({
    mutationFn: () => api.pushAnnouncement(id, annoMsg, annoDur),
    onSuccess: () => { setAnnoMsg(''); setFeedback('Announcement sent'); },
  });

  const refreshMutation = useMutation({
    mutationFn: () => api.pushRefresh(id),
    onSuccess: () => setFeedback('Refresh sent'),
  });

  const rebootMutation = useMutation({
    mutationFn: () => api.rebootDevice(id),
    onSuccess: () => setFeedback('Reboot command sent'),
  });

  if (isLoading) return (
    <div className="flex justify-center py-20">
      <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
    </div>
  );
  if (!room) return <p className="text-red-500">Room not found</p>;

  const guest    = room.guests?.[0];
  const online   = isOnline(room.lastSeenAt);
  const occupied = !!guest;

  return (
    <div className="max-w-3xl space-y-5">
      {/* Back */}
      <Link href="/dashboard/rooms" className="flex items-center gap-1.5 text-sm text-slate-500 hover:text-slate-800">
        <ArrowLeft size={16} /> All Rooms
      </Link>

      {/* Room header */}
      <div className="bg-white rounded-xl border border-slate-200 p-5">
        <div className="flex items-start justify-between">
          <div>
            <h2 className="text-2xl font-bold text-slate-800">Room {room.roomNumber}</h2>
            {room.floor && <p className="text-slate-500 text-sm">Floor {room.floor}</p>}
          </div>
          <div className="flex flex-col items-end gap-1.5">
            <StatusPill online={online} occupied={occupied} />
            <span className="text-xs text-slate-400 capitalize">{room.deviceType.replace('_', ' ')}</span>
          </div>
        </div>

        {/* Guest info */}
        {guest ? (
          <div className="mt-4 pt-4 border-t border-slate-100 grid grid-cols-2 gap-3 text-sm">
            <Info label="Guest"    value={`${guest.guestFirstName} ${guest.guestLastName}`} />
            <Info label="Language" value={guest.guestLanguage.toUpperCase()} />
            <Info label="Check-in"  value={guest.checkIn} />
            <Info label="Check-out" value={guest.checkOut} />
          </div>
        ) : (
          <p className="mt-3 text-sm text-slate-400">No guest currently checked in.</p>
        )}

        {room.lastSeenAt && (
          <p className="mt-3 text-xs text-slate-400">
            Last ping: {new Date(room.lastSeenAt).toLocaleString()}
          </p>
        )}
      </div>

      {/* Background override */}
      <div className="bg-white rounded-xl border border-slate-200 p-5">
        <h3 className="font-semibold text-slate-800 flex items-center gap-2 mb-3">
          <Image size={16} /> Override Background
        </h3>
        {room.backgroundUrl && (
          <p className="text-xs text-slate-500 mb-2 truncate">Current: {room.backgroundUrl}</p>
        )}
        <div className="flex gap-2">
          <input
            type="url"
            value={bgUrl}
            onChange={e => setBgUrl(e.target.value)}
            placeholder="https://… image URL"
            className="flex-1 text-sm px-3 py-2 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            onClick={() => bgMutation.mutate(bgUrl)}
            disabled={!bgUrl || bgMutation.isPending}
            className="px-4 py-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 text-white text-sm font-medium rounded-lg"
          >
            Apply
          </button>
        </div>
      </div>

      {/* Send announcement */}
      <div className="bg-white rounded-xl border border-slate-200 p-5">
        <h3 className="font-semibold text-slate-800 flex items-center gap-2 mb-3">
          <Megaphone size={16} /> Send Announcement
        </h3>
        <textarea
          value={annoMsg}
          onChange={e => setAnnoMsg(e.target.value)}
          placeholder="Message to display on the TV…"
          rows={3}
          className="w-full text-sm px-3 py-2 border border-slate-200 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-500 mb-2"
        />
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm text-slate-600">
            <span>Duration:</span>
            <input
              type="number" min={5} max={120} value={annoDur}
              onChange={e => setAnnoDur(Number(e.target.value))}
              className="w-16 px-2 py-1 border border-slate-200 rounded text-center focus:outline-none focus:ring-1 focus:ring-blue-500"
            />
            <span>seconds</span>
          </div>
          <button
            onClick={() => annoMutation.mutate()}
            disabled={!annoMsg || annoMutation.isPending}
            className="px-4 py-2 bg-amber-500 hover:bg-amber-400 disabled:opacity-50 text-white text-sm font-medium rounded-lg"
          >
            Send
          </button>
        </div>
      </div>

      {/* Quick actions */}
      <div className="bg-white rounded-xl border border-slate-200 p-5">
        <h3 className="font-semibold text-slate-800 mb-3">Quick Actions</h3>
        <div className="flex flex-wrap gap-3">
          <ActionBtn
            icon={RefreshCw}
            label="Force Refresh"
            color="blue"
            loading={refreshMutation.isPending}
            onClick={() => refreshMutation.mutate()}
          />
          <ActionBtn
            icon={Power}
            label="Reboot TV App"
            color="red"
            loading={rebootMutation.isPending}
            onClick={() => rebootMutation.mutate()}
          />
        </div>
      </div>

      {feedback && (
        <div className="bg-emerald-50 border border-emerald-200 rounded-xl px-4 py-3 text-emerald-700 text-sm">
          ✓ {feedback}
        </div>
      )}
    </div>
  );
}

function StatusPill({ online, occupied }: { online: boolean; occupied: boolean }) {
  return (
    <span className={clsx(
      'px-2.5 py-1 rounded-full text-xs font-semibold',
      !online   ? 'bg-red-100 text-red-700'
        : occupied ? 'bg-emerald-100 text-emerald-700'
        :            'bg-slate-100 text-slate-600',
    )}>
      {!online ? 'Offline' : occupied ? 'Occupied' : 'Vacant'}
    </span>
  );
}

function Info({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <p className="text-xs text-slate-500 mb-0.5">{label}</p>
      <p className="font-medium text-slate-800">{value}</p>
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
      className={clsx(
        'flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium disabled:opacity-50 transition-colors',
        color === 'blue' ? 'bg-blue-50 text-blue-700 hover:bg-blue-100'
                        : 'bg-red-50 text-red-700 hover:bg-red-100',
      )}
    >
      <Icon size={15} className={loading ? 'animate-spin' : ''} />
      {label}
    </button>
  );
}
