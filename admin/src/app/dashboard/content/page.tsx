'use client';
import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import type { HotelContent } from '@/types';
import { Plus, Megaphone, Image, ToggleLeft, ToggleRight, Trash2 } from 'lucide-react';
import { clsx } from 'clsx';

const TYPES = ['announcement', 'background', 'promo'] as const;
type ContentType = typeof TYPES[number];

export default function ContentPage() {
  const qc = useQueryClient();
  const [tab,       setTab]      = useState<ContentType>('announcement');
  const [showForm,  setShowForm] = useState(false);
  const [form,      setForm]     = useState({ titleEn: '', bodyEn: '', mediaUrl: '', priority: 'info' });

  const { data: items = [], isLoading } = useQuery({
    queryKey: ['content'],
    queryFn:  api.getContent,
  });

  const filtered = items.filter((i: HotelContent) => i.contentType === tab);

  const createMutation = useMutation({
    mutationFn: (data: Partial<HotelContent>) => api.createContent(data),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['content'] }); setShowForm(false); setForm({ titleEn: '', bodyEn: '', mediaUrl: '', priority: 'info' }); },
  });

  const toggleMutation = useMutation({
    mutationFn: ({ id, active }: { id: string; active: boolean }) => api.updateContent(id, { active }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['content'] }),
  });

  function submit(e: React.FormEvent) {
    e.preventDefault();
    createMutation.mutate({
      contentType: tab,
      title: form.titleEn ? { en: form.titleEn } : undefined,
      body:  form.bodyEn  ? { en: form.bodyEn }  : undefined,
      mediaUrl: form.mediaUrl || undefined,
      priority: form.priority,
      active: true,
    });
  }

  return (
    <div className="max-w-3xl space-y-5">
      {/* Tabs */}
      <div className="flex gap-1 bg-slate-100 p-1 rounded-lg w-fit">
        {TYPES.map(t => (
          <button
            key={t}
            onClick={() => { setTab(t); setShowForm(false); }}
            className={clsx(
              'px-4 py-1.5 rounded-md text-sm font-medium capitalize transition-colors',
              tab === t ? 'bg-white text-slate-800 shadow-sm' : 'text-slate-500 hover:text-slate-700',
            )}
          >{t}</button>
        ))}
      </div>

      {/* Add button */}
      <div className="flex justify-end">
        <button
          onClick={() => setShowForm(s => !s)}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium rounded-lg"
        >
          <Plus size={16} /> New {tab}
        </button>
      </div>

      {/* Create form */}
      {showForm && (
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <form onSubmit={submit} className="space-y-3">
            <Field label="Title (EN)" value={form.titleEn} onChange={v => setForm(f => ({ ...f, titleEn: v }))} />
            <Field label="Body (EN)"  value={form.bodyEn}  onChange={v => setForm(f => ({ ...f, bodyEn: v }))}  textarea />
            <Field label="Media URL"  value={form.mediaUrl} onChange={v => setForm(f => ({ ...f, mediaUrl: v }))} />
            {tab === 'announcement' && (
              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1">Priority</label>
                <select
                  value={form.priority}
                  onChange={e => setForm(f => ({ ...f, priority: e.target.value }))}
                  className="text-sm px-3 py-2 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="info">Info</option>
                  <option value="warning">Warning</option>
                  <option value="promo">Promo</option>
                </select>
              </div>
            )}
            <div className="flex gap-2 justify-end pt-2">
              <button type="button" onClick={() => setShowForm(false)} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg">Cancel</button>
              <button type="submit" disabled={createMutation.isPending}
                className="px-4 py-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 text-white text-sm font-medium rounded-lg">Create</button>
            </div>
          </form>
        </div>
      )}

      {/* Items list */}
      {isLoading ? (
        <Spinner />
      ) : filtered.length === 0 ? (
        <EmptyState type={tab} />
      ) : (
        <div className="space-y-3">
          {filtered.map((item: HotelContent) => (
            <ContentCard key={item.id} item={item} onToggle={v => toggleMutation.mutate({ id: item.id, active: v })} />
          ))}
        </div>
      )}
    </div>
  );
}

function ContentCard({ item, onToggle }: { item: HotelContent; onToggle: (v: boolean) => void }) {
  const title = typeof item.title === 'object' ? Object.values(item.title as Record<string,string>)[0] : item.title;
  const body  = typeof item.body  === 'object' ? Object.values(item.body  as Record<string,string>)[0] : item.body;

  return (
    <div className={clsx(
      'bg-white rounded-xl border p-4 flex items-start gap-3',
      item.active ? 'border-slate-200' : 'border-slate-100 opacity-60',
    )}>
      <div className="w-8 h-8 rounded-lg bg-slate-100 flex items-center justify-center shrink-0">
        {item.contentType === 'announcement' ? <Megaphone size={15} className="text-slate-500" /> : <Image size={15} className="text-slate-500" />}
      </div>
      <div className="flex-1 min-w-0">
        {title && <p className="font-medium text-slate-800 text-sm">{title}</p>}
        {body  && <p className="text-xs text-slate-500 mt-0.5 line-clamp-2">{body}</p>}
        {item.mediaUrl && (
          <a href={item.mediaUrl} target="_blank" rel="noopener" className="text-xs text-blue-500 hover:underline mt-0.5 block truncate">
            {item.mediaUrl}
          </a>
        )}
        <p className="text-xs text-slate-400 mt-1">{new Date(item.createdAt).toLocaleDateString()}</p>
      </div>
      <div className="flex items-center gap-2 shrink-0">
        {item.priority && (
          <PriorityBadge priority={item.priority} />
        )}
        <button onClick={() => onToggle(!item.active)} className="text-slate-400 hover:text-slate-700">
          {item.active ? <ToggleRight size={20} className="text-emerald-500" /> : <ToggleLeft size={20} />}
        </button>
      </div>
    </div>
  );
}

function PriorityBadge({ priority }: { priority: string }) {
  return (
    <span className={clsx(
      'px-2 py-0.5 rounded-full text-xs font-medium capitalize',
      priority === 'warning' ? 'bg-amber-100 text-amber-700'
        : priority === 'promo' ? 'bg-violet-100 text-violet-700'
        : 'bg-blue-100 text-blue-700',
    )}>{priority}</span>
  );
}

function Field({ label, value, onChange, textarea }: {
  label: string; value: string; onChange: (v: string) => void; textarea?: boolean;
}) {
  const cls = 'w-full text-sm px-3 py-2 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500';
  return (
    <div>
      <label className="block text-xs font-medium text-slate-600 mb-1">{label}</label>
      {textarea
        ? <textarea value={value} onChange={e => onChange(e.target.value)} rows={3} className={clsx(cls, 'resize-none')} />
        : <input type="text" value={value} onChange={e => onChange(e.target.value)} className={cls} />}
    </div>
  );
}

function Spinner() { return <div className="flex justify-center py-10"><div className="w-6 h-6 border-4 border-blue-600 border-t-transparent rounded-full animate-spin"/></div>; }
function EmptyState({ type }: { type: string }) { return <div className="text-center py-10 text-slate-400 text-sm">No {type} items yet.</div>; }
