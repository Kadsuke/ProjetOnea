import { IAgent } from 'app/entities/agent/agent.model';
import { ICentre } from 'app/entities/centre/centre.model';

export interface ISite {
  id?: number;
  libelle?: string;
  responsable?: string;
  contact?: string;
  agents?: IAgent[] | null;
  centre?: ICentre | null;
  agents?: IAgent[] | null;
}

export class Site implements ISite {
  constructor(
    public id?: number,
    public libelle?: string,
    public responsable?: string,
    public contact?: string,
    public agents?: IAgent[] | null,
    public centre?: ICentre | null,
    public agents?: IAgent[] | null
  ) {}
}

export function getSiteIdentifier(site: ISite): number | undefined {
  return site.id;
}
