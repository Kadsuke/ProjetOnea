import { IParcelle } from 'app/entities/parcelle/parcelle.model';
import { ISection } from 'app/entities/section/section.model';

export interface ILot {
  id?: number;
  libelle?: string;
  parcelles?: IParcelle[] | null;
  section?: ISection | null;
}

export class Lot implements ILot {
  constructor(public id?: number, public libelle?: string, public parcelles?: IParcelle[] | null, public section?: ISection | null) {}
}

export function getLotIdentifier(lot: ILot): number | undefined {
  return lot.id;
}
