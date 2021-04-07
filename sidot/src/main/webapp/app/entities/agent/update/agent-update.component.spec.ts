jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AgentService } from '../service/agent.service';
import { IAgent, Agent } from '../agent.model';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { AgentUpdateComponent } from './agent-update.component';

describe('Component Tests', () => {
  describe('Agent Management Update Component', () => {
    let comp: AgentUpdateComponent;
    let fixture: ComponentFixture<AgentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let agentService: AgentService;
    let siteService: SiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AgentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AgentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AgentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      agentService = TestBed.inject(AgentService);
      siteService = TestBed.inject(SiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Site query and add missing value', () => {
        const agent: IAgent = { id: 456 };
        const site: ISite = { id: 46914 };
        agent.site = site;
        const site: ISite = { id: 63986 };
        agent.site = site;

        const siteCollection: ISite[] = [{ id: 51332 }];
        spyOn(siteService, 'query').and.returnValue(of(new HttpResponse({ body: siteCollection })));
        const additionalSites = [site, site];
        const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
        spyOn(siteService, 'addSiteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ agent });
        comp.ngOnInit();

        expect(siteService.query).toHaveBeenCalled();
        expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
        expect(comp.sitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const agent: IAgent = { id: 456 };
        const site: ISite = { id: 32048 };
        agent.site = site;
        const site: ISite = { id: 87210 };
        agent.site = site;

        activatedRoute.data = of({ agent });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(agent));
        expect(comp.sitesSharedCollection).toContain(site);
        expect(comp.sitesSharedCollection).toContain(site);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agent = { id: 123 };
        spyOn(agentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: agent }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(agentService.update).toHaveBeenCalledWith(agent);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agent = new Agent();
        spyOn(agentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: agent }));
        saveSubject.complete();

        // THEN
        expect(agentService.create).toHaveBeenCalledWith(agent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const agent = { id: 123 };
        spyOn(agentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ agent });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(agentService.update).toHaveBeenCalledWith(agent);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSiteById', () => {
        it('Should return tracked Site primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSiteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
