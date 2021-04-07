jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SiteService } from '../service/site.service';
import { ISite, Site } from '../site.model';
import { ICentre } from 'app/entities/centre/centre.model';
import { CentreService } from 'app/entities/centre/service/centre.service';

import { SiteUpdateComponent } from './site-update.component';

describe('Component Tests', () => {
  describe('Site Management Update Component', () => {
    let comp: SiteUpdateComponent;
    let fixture: ComponentFixture<SiteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let siteService: SiteService;
    let centreService: CentreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      siteService = TestBed.inject(SiteService);
      centreService = TestBed.inject(CentreService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Centre query and add missing value', () => {
        const site: ISite = { id: 456 };
        const centre: ICentre = { id: 43000 };
        site.centre = centre;

        const centreCollection: ICentre[] = [{ id: 7402 }];
        spyOn(centreService, 'query').and.returnValue(of(new HttpResponse({ body: centreCollection })));
        const additionalCentres = [centre];
        const expectedCollection: ICentre[] = [...additionalCentres, ...centreCollection];
        spyOn(centreService, 'addCentreToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ site });
        comp.ngOnInit();

        expect(centreService.query).toHaveBeenCalled();
        expect(centreService.addCentreToCollectionIfMissing).toHaveBeenCalledWith(centreCollection, ...additionalCentres);
        expect(comp.centresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const site: ISite = { id: 456 };
        const centre: ICentre = { id: 25715 };
        site.centre = centre;

        activatedRoute.data = of({ site });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(site));
        expect(comp.centresSharedCollection).toContain(centre);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = { id: 123 };
        spyOn(siteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: site }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(siteService.update).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = new Site();
        spyOn(siteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: site }));
        saveSubject.complete();

        // THEN
        expect(siteService.create).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = { id: 123 };
        spyOn(siteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(siteService.update).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCentreById', () => {
        it('Should return tracked Centre primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCentreById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
