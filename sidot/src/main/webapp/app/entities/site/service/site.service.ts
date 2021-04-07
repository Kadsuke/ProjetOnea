import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISite, getSiteIdentifier } from '../site.model';

export type EntityResponseType = HttpResponse<ISite>;
export type EntityArrayResponseType = HttpResponse<ISite[]>;

@Injectable({ providedIn: 'root' })
export class SiteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sites');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(site: ISite): Observable<EntityResponseType> {
    return this.http.post<ISite>(this.resourceUrl, site, { observe: 'response' });
  }

  update(site: ISite): Observable<EntityResponseType> {
    return this.http.put<ISite>(`${this.resourceUrl}/${getSiteIdentifier(site) as number}`, site, { observe: 'response' });
  }

  partialUpdate(site: ISite): Observable<EntityResponseType> {
    return this.http.patch<ISite>(`${this.resourceUrl}/${getSiteIdentifier(site) as number}`, site, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSiteToCollectionIfMissing(siteCollection: ISite[], ...sitesToCheck: (ISite | null | undefined)[]): ISite[] {
    const sites: ISite[] = sitesToCheck.filter(isPresent);
    if (sites.length > 0) {
      const siteCollectionIdentifiers = siteCollection.map(siteItem => getSiteIdentifier(siteItem)!);
      const sitesToAdd = sites.filter(siteItem => {
        const siteIdentifier = getSiteIdentifier(siteItem);
        if (siteIdentifier == null || siteCollectionIdentifiers.includes(siteIdentifier)) {
          return false;
        }
        siteCollectionIdentifiers.push(siteIdentifier);
        return true;
      });
      return [...sitesToAdd, ...siteCollection];
    }
    return siteCollection;
  }
}
