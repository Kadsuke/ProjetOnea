import { ICentreRegroupement } from 'app/entities/centre-regroupement/centre-regroupement.model';

export interface IDirectionRegionale {
  id?: number;
  libelle?: string;
  responsable?: string;
  contact?: string;
  centreRegroupements?: ICentreRegroupement[] | null;
}

export class DirectionRegionale implements IDirectionRegionale {
  constructor(
    public id?: number,
    public libelle?: string,
    public responsable?: string,
    public contact?: string,
    public centreRegroupements?: ICentreRegroupement[] | null
  ) {}
}

export function getDirectionRegionaleIdentifier(directionRegionale: IDirectionRegionale): number | undefined {
  return directionRegionale.id;
}
