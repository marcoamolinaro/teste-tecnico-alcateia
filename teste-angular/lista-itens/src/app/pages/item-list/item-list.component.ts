import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Item } from '../../models/item';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-item-list',
  imports: [CommonModule, RouterLink],
  template: `
    <section class="container">
      <h1>Lista de itens</h1>

      @if (loading()) {
        <p>Carregando...</p>
      } @else {
        @if (items().length > 0) {
          <ul>
            @for (item of items(); track item.id) {
              <li>
                <a [routerLink]="['/items', item.id]">
                  {{ item.nome }} - {{ item.preco | currency: 'BRL' }}
                </a>
              </li>
            }
          </ul>
        } @else {
          <p>Nenhum item disponível.</p>
        }
      }
    </section>
  `,
  styles: `
    .container {
      max-width: 720px;
      margin: 2rem auto;
      padding: 0 1rem;
      font-family: Arial, sans-serif;
    }

    h1 {
      margin-bottom: 1rem;
    }

    ul {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    li {
      margin-bottom: 0.75rem;
    }

    a {
      text-decoration: none;
      color: #0055aa;
      font-weight: 600;
    }

    a:hover {
      text-decoration: underline;
    }
  `
})
export class ItemListComponent implements OnInit {
  private readonly itemService = inject(ItemService);

  items = signal<Item[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.itemService.getItems().subscribe({
      next: (items) => {
        this.items.set(items);
        this.loading.set(false);
      },
      error: () => {
        this.items.set([]);
        this.loading.set(false);
      }
    });
  }
}
