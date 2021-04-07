import { ISite } from 'app/entities/site/site.model';
import { ICentreRegroupement } from 'app/entities/centre-regroupement/centre-regroupement.model';

export interface ICentre {
  id?: number;
  libelle?: string;
  responsable?: string;
  contact?: string;
  sites?: ISite[] | null;
  centreRegroupement?: ICentreRegroupement | null;
}

export class Centre implements ICentre {
  constructor(
    public id?: number,
    public libelle?: string,
    public responsable?: string,
    public contact?: string,
    public sites?: ISite[] | null,
    public centreRegroupement?: ICentreRegroupement | null
  ) {}
}

export function getCentreIdentifier(centre: ICentre): number | undefined {
  return centre.id;
}
