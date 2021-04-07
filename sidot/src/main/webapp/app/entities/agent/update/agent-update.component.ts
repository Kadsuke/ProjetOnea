import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAgent, Agent } from '../agent.model';
import { AgentService } from '../service/agent.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

@Component({
  selector: 'jhi-agent-update',
  templateUrl: './agent-update.component.html',
})
export class AgentUpdateComponent implements OnInit {
  isSaving = false;

  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    numero: [null, [Validators.required]],
    role: [null, [Validators.required]],
    site: [],
    site: [],
  });

  constructor(
    protected agentService: AgentService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.updateForm(agent);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agent = this.createFromForm();
    if (agent.id !== undefined) {
      this.subscribeToSaveResponse(this.agentService.update(agent));
    } else {
      this.subscribeToSaveResponse(this.agentService.create(agent));
    }
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgent>>): void {
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

  protected updateForm(agent: IAgent): void {
    this.editForm.patchValue({
      id: agent.id,
      nom: agent.nom,
      numero: agent.numero,
      role: agent.role,
      site: agent.site,
      site: agent.site,
    });

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, agent.site, agent.site);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(
        map((sites: ISite[]) =>
          this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('site')!.value, this.editForm.get('site')!.value)
        )
      )
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IAgent {
    return {
      ...new Agent(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      role: this.editForm.get(['role'])!.value,
      site: this.editForm.get(['site'])!.value,
      site: this.editForm.get(['site'])!.value,
    };
  }
}
