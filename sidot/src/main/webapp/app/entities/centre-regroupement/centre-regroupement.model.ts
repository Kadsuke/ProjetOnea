import { ICentre } from 'app/entities/centre/centre.model';
import { IDirectionRegionale } from 'app/entities/direction-regionale/direction-regionale.model';

export interface ICentreRegroupement {
  id?: number;
  libelle?: string;
  responsable?: string;
  contact?: string;
  centres?: ICentre[] | null;
  directionRegionale?: IDirectionRegionale | null;
}

export class CentreRegroupement implements ICentreRegroupement {
  constructor(
    public id?: number,
    public libelle?: string,
    public responsable?: string,
    public contact?: string,
    public centres?: ICentre[] | null,
    public directionRegionale?: IDirectionRegionale | null
  ) {}
}

export function getCentreRegroupementIdentifier(centreRegroupement: ICentreRegroupement): number | undefined {
  return centreRegroupement.id;
}
