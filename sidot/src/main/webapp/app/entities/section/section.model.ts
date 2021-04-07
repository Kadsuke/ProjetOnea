import { ILot } from 'app/entities/lot/lot.model';
import { ISecteur } from 'app/entities/secteur/secteur.model';

export interface ISection {
  id?: number;
  libelle?: string;
  lots?: ILot[] | null;
  secteur?: ISecteur | null;
}

export class Section implements ISection {
  constructor(public id?: number, public libelle?: string, public lots?: ILot[] | null, public secteur?: ISecteur | null) {}
}

export function getSectionIdentifier(section: ISection): number | undefined {
  return section.id;
}
