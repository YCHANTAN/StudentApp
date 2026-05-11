export type ProgramCategory = 'Undergraduate' | 'Postgraduate';
export type BadgeVariant = 'Accent' | 'Primary';

export type Program = {
  id: string;
  title: string;
  badgeText: string;
  badgeVariant: BadgeVariant;
  scheduleLine: string;
  description: string;
  category: ProgramCategory;
  prospectusUrl?: string;
  createdAt: Date;
};
