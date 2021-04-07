import { ICommune } from 'app/entities/commune/commune.model';

export interface ITypeCommune {
  id?: number;
  libelle?: string;
  communes?: ICommune[] | null;
}

export class TypeCommune implements ITypeCommune {
  constructor(public id?: number, public libelle?: string, public communes?: ICommune[] | null) {}
}

export function getTypeCommuneIdentifier(typeCommune: ITypeCommune): number | undefined {
  return typeCommune.id;
}
