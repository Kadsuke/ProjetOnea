import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISite, Site } from '../site.model';
import { SiteService } from '../service/site.service';
import { ICentre } from 'app/entities/centre/centre.model';
import { CentreService } from 'app/entities/centre/service/centre.service';

@Component({
  selector: 'jhi-site-update',
  templateUrl: './site-update.component.html',
})
export class SiteUpdateComponent implements OnInit {
  isSaving = false;

  centresSharedCollection: ICentre[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required]],
    responsable: [null, [Validators.required]],
    contact: [null, [Validators.required]],
    centre: [],
  });

  constructor(
    protected siteService: SiteService,
    protected centreService: CentreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ site }) => {
      this.updateForm(site);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const site = this.createFromForm();
    if (site.id !== undefined) {
      this.subscribeToSaveResponse(this.siteService.update(site));
    } else {
      this.subscribeToSaveResponse(this.siteService.create(site));
    }
  }

  trackCentreById(index: number, item: ICentre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISite>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(site: ISite): void {
    this.editForm.patchValue({
      id: site.id,
      libelle: site.libelle,
      responsable: site.responsable,
      contact: site.contact,
      centre: site.centre,
    });

    this.centresSharedCollection = this.centreService.addCentreToCollectionIfMissing(this.centresSharedCollection, site.centre);
  }

  protected loadRelationshipsOptions(): void {
    this.centreService
      .query()
      .pipe(map((res: HttpResponse<ICentre[]>) => res.body ?? []))
      .pipe(map((centres: ICentre[]) => this.centreService.addCentreToCollectionIfMissing(centres, this.editForm.get('centre')!.value)))
      .subscribe((centres: ICentre[]) => (this.centresSharedCollection = centres));
  }

  protected createFromForm(): ISite {
    return {
      ...new Site(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      responsable: this.editForm.get(['responsable'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      centre: this.editForm.get(['centre'])!.value,
    };
  }
}
