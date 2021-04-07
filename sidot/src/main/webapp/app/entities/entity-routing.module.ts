import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'centre-regroupement',
        data: { pageTitle: 'sidotApp.centreRegroupement.home.title' },
        loadChildren: () => import('./centre-regroupement/centre-regroupement.module').then(m => m.CentreRegroupementModule),
      },
      {
        path: 'site',
        data: { pageTitle: 'sidotApp.site.home.title' },
        loadChildren: () => import('./site/site.module').then(m => m.SiteModule),
      },
      {
        path: 'lot',
        data: { pageTitle: 'sidotApp.lot.home.title' },
        loadChildren: () => import('./lot/lot.module').then(m => m.LotModule),
      },
      {
        path: 'parcelle',
        data: { pageTitle: 'sidotApp.parcelle.home.title' },
        loadChildren: () => import('./parcelle/parcelle.module').then(m => m.ParcelleModule),
      },
      {
        path: 'centre',
        data: { pageTitle: 'sidotApp.centre.home.title' },
        loadChildren: () => import('./centre/centre.module').then(m => m.CentreModule),
      },
      {
        path: 'direction-regionale',
        data: { pageTitle: 'sidotApp.directionRegionale.home.title' },
        loadChildren: () => import('./direction-regionale/direction-regionale.module').then(m => m.DirectionRegionaleModule),
      },
      {
        path: 'type-commune',
        data: { pageTitle: 'sidotApp.typeCommune.home.title' },
        loadChildren: () => import('./type-commune/type-commune.module').then(m => m.TypeCommuneModule),
      },
      {
        path: 'secteur',
        data: { pageTitle: 'sidotApp.secteur.home.title' },
        loadChildren: () => import('./secteur/secteur.module').then(m => m.SecteurModule),
      },
      {
        path: 'commune',
        data: { pageTitle: 'sidotApp.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'sidotApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'sidotApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'agent',
        data: { pageTitle: 'sidotApp.agent.home.title' },
        loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
      },
      {
        path: 'localite',
        data: { pageTitle: 'sidotApp.localite.home.title' },
        loadChildren: () => import('./localite/localite.module').then(m => m.LocaliteModule),
      },
      {
        path: 'section',
        data: { pageTitle: 'sidotApp.section.home.title' },
        loadChildren: () => import('./section/section.module').then(m => m.SectionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
