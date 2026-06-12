'use client';
import { usePathname } from 'next/navigation';
import { Bell } from 'lucide-react';

const TITLES: Record<string, string> = {
  '/dashboard':          'Overview',
  '/dashboard/rooms':    'Rooms',
  '/dashboard/services': 'Services',
  '/dashboard/content':  'Content',
  '/dashboard/devices':  'Devices',
  '/dashboard/settings': 'Settings',
};

export function Header() {
  const pathname = usePathname();
  const base = '/' + pathname.split('/').slice(1, 3).join('/');
  const title = TITLES[base] ?? 'Admin';

  return (
    <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6 shrink-0">
      <h1 className="text-lg font-semibold text-slate-800">{title}</h1>
      <div className="flex items-center gap-3">
        <button className="relative p-2 rounded-lg hover:bg-slate-100 transition-colors">
          <Bell size={18} className="text-slate-500" />
        </button>
        <div className="w-8 h-8 rounded-full bg-blue-600 flex items-center justify-center">
          <span className="text-white text-xs font-bold">A</span>
        </div>
      </div>
    </header>
  );
}
