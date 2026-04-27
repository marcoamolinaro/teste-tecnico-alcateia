import { Routes } from '@angular/router';
import { ItemDetailComponent } from './pages/item-detail/item-detail.component';
import { ItemListComponent } from './pages/item-list/item-list.component';

export const routes: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'items'
	},
	{
		path: 'items',
		component: ItemListComponent
	},
	{
		path: 'items/:id',
		component: ItemDetailComponent
	},
	{
		path: '**',
		redirectTo: 'items'
	}
];
