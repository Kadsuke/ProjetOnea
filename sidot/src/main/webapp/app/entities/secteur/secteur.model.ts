import { ISection } from 'app/entities/section/section.model';
import { ILocalite } from 'app/entities/localite/localite.model';

export interface ISecteur {
  id?: number;
  libelle?: string;
  sections?: ISection[] | null;
  localite?: ILocalite | null;
}

export class Secteur implements ISecteur {
  constructor(public id?: number, public libelle?: string, public sections?: ISection[] | null, public localite?: ILocalite | null) {}
}

export function getSecteurIdentifier(secteur: ISecteur): number | undefined {
  return secteur.id;
}
