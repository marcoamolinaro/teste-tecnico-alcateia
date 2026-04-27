import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { Item } from '../../models/item';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-item-detail',
  imports: [CommonModule, CurrencyPipe, RouterLink],
  template: `
    <section class="container">
      <h1>Detalhe do item</h1>

      @if (loading()) {
        <p>Carregando...</p>
      } @else {
        @if (errorMessage()) {
          <p>{{ errorMessage() }}</p>
        } @else if (item()) {
          <h2>{{ item()!.nome }}</h2>
          <p><strong>ID:</strong> {{ item()!.id }}</p>
          <p><strong>Descrição:</strong> {{ item()!.descricao }}</p>
          <p><strong>Preço:</strong> {{ item()!.preco | currency: 'BRL' }}</p>
        }
      }

      <p>
        <a routerLink="/items">Voltar para lista</a>
      </p>
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

    h2 {
      margin-bottom: 0.5rem;
    }

    p {
      margin-bottom: 0.5rem;
    }

    a {
      color: #0055aa;
      text-decoration: none;
      font-weight: 600;
    }

    a:hover {
      text-decoration: underline;
    }
  `
})
export class ItemDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly itemService = inject(ItemService);

  item = signal<Item | undefined>(undefined);
  loading = signal(true);
  errorMessage = signal('');

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || Number.isNaN(id)) {
      this.loading.set(false);
      this.errorMessage.set('Item não encontrado');
      return;
    }

    this.itemService.getItemById(id).subscribe({
      next: (item) => {
        this.item.set(item);
        this.loading.set(false);

        if (!item) {
          this.errorMessage.set('Item não encontrado');
        }
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Item não encontrado');
      }
    });
  }
}
