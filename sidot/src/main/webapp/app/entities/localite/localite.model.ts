import { ISecteur } from 'app/entities/secteur/secteur.model';
import { ICommune } from 'app/entities/commune/commune.model';

export interface ILocalite {
  id?: number;
  libelle?: string;
  secteurs?: ISecteur[] | null;
  commune?: ICommune | null;
}

export class Localite implements ILocalite {
  constructor(public id?: number, public libelle?: string, public secteurs?: ISecteur[] | null, public commune?: ICommune | null) {}
}

export function getLocaliteIdentifier(localite: ILocalite): number | undefined {
  return localite.id;
}
