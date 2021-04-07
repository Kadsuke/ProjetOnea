import { ISite } from 'app/entities/site/site.model';

export interface IAgent {
  id?: number;
  nom?: string;
  numero?: string;
  role?: string;
  site?: ISite | null;
  site?: ISite | null;
}

export class Agent implements IAgent {
  constructor(
    public id?: number,
    public nom?: string,
    public numero?: string,
    public role?: string,
    public site?: ISite | null,
    public site?: ISite | null
  ) {}
}

export function getAgentIdentifier(agent: IAgent): number | undefined {
  return agent.id;
}
