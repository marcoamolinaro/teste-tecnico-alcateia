import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';

import { Item } from '../models/item';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private readonly items: Item[] = [
    {
      id: 1,
      nome: 'Notebook',
      descricao: 'Notebook para trabalho e estudos com 16GB de RAM.',
      preco: 4299.9
    },
    {
      id: 2,
      nome: 'Mouse sem fio',
      descricao: 'Mouse ergonômico com conexão Bluetooth.',
      preco: 149.9
    },
    {
      id: 3,
      nome: 'Teclado mecânico',
      descricao: 'Teclado ABNT2 com switches táteis.',
      preco: 329.9
    }
  ];

  getItems(): Observable<Item[]> {
    return of(this.items).pipe(delay(700));
  }

  getItemById(id: number): Observable<Item | undefined> {
    const item = this.items.find((currentItem) => currentItem.id === id);
    return of(item).pipe(delay(700));
  }
}
