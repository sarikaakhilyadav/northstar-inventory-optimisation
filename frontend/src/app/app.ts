import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  InventoryAnalytics,
  CategoryRevenue,
  InventoryRisk,
  InventoryPriority,
  Product,
  InventoryAnalyticsService
} from './services/inventory-analytics';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})

export class App implements OnInit {
  private readonly analyticsService = inject(InventoryAnalyticsService);

  readonly analytics = signal<InventoryAnalytics | null>(null);
  readonly loading = signal(true);
  readonly error = signal('');
  readonly categoryRevenue = signal<CategoryRevenue[]>([]);
  readonly inventoryRisks = signal<InventoryRisk[]>([]);
  readonly inventoryPriorities = signal<InventoryPriority[]>([]);
  readonly products = signal<Product[]>([]);

  newProduct: Omit<Product, 'productId'> = {
    productName: '',
    category: '',
    costPrice: 0,
    sellingPrice: 0,
    currentStock: 0,
    reorderLevel: 0,
    unitsSold: 0
  };

  ngOnInit(): void {
    this.analyticsService.getAnalytics().subscribe({
      next: (data) => {
        this.analytics.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load inventory analytics:', err);
        this.error.set('Unable to load inventory analytics.');
        this.loading.set(false);
      }
    });
    this.analyticsService.getCategoryRevenue().subscribe({
      next: (data) => {
        this.categoryRevenue.set(data);
        console.log('Category revenue loaded:', data);
      },
      error: (err) => {
        console.error('Failed to load category revenue:', err);
      }
    });
    this.analyticsService.getInventoryRisks().subscribe({
      next: (data) => {
        this.inventoryRisks.set(data);
        console.log('Inventory risks loaded:', data);
      },
      error: (err) => {
        console.error('Failed to load inventory risks:', err);
      }
    });
    this.analyticsService.getInventoryPriorities().subscribe({
      next: (data) => {
        this.inventoryPriorities.set(data);
        console.log('Inventory priorities loaded:', data);
      },
      error: (err) => {
        console.error('Failed to load inventory priorities:', err);
      }
    });
    this.analyticsService.getProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        console.log('Products loaded:', data);
      },
      error: (err) => {
        console.error('Failed to load products:', err);
      }
    });

  }


  getRevenuePercentage(revenue: number): number {
    const revenues = this.categoryRevenue().map(item => item.revenue);
    const highestRevenue = Math.max(...revenues, 0);

    if (highestRevenue === 0) {
      return 0;
    }

    return (revenue / highestRevenue) * 100;
  }


  restockProduct(product: Product): void {
    const input = window.prompt(
      `Enter quantity to restock for ${product.productName}:`
    );

    // Stop if the user cancels
    if (input === null) {
      return;
    }

    const quantity = Number(input);

    // Validate the entered quantity
    if (!Number.isSafeInteger(quantity) || quantity <= 0) {
      window.alert('Please enter a valid positive whole number.');
      return;
    }

    // Send the restock request to Spring Boot
    this.analyticsService.restockProduct(
      product.productId,
      quantity
    ).subscribe({
      next: () => {
        window.alert(`${product.productName} restocked successfully!`);

        // Refresh all dashboard data
        this.refreshDashboard();
      },
      error: (err) => {
        console.error('Restock failed:', err);
        window.alert('Restock failed. Please try again.');
      }
    });
  }


  sellProduct(product: Product): void {
    const input = window.prompt(
      `Enter quantity to sell for ${product.productName}:`
    );

    // Stop if the user cancels
    if (input === null) {
      return;
    }

    const quantity = Number(input);

    // Validate the entered quantity
    if (!Number.isSafeInteger(quantity) || quantity <= 0) {
      window.alert('Please enter a valid positive whole number.');
      return;
    }

    // Check available stock
    if (quantity > product.currentStock) {
      window.alert(
        `Insufficient stock! Only ${product.currentStock} units available.`
      );
      return;
    }

    // Send the sale request to Spring Boot
    this.analyticsService.sellProduct(
      product.productId,
      quantity
    ).subscribe({
      next: () => {
        window.alert(
          `${quantity} unit(s) of ${product.productName} sold successfully!`
        );

        // Refresh all dashboard information
        this.refreshDashboard();
      },
      error: (err) => {
        console.error('Sale failed:', err);

        if (err.status === 409) {
          window.alert(
            'Sale rejected: insufficient stock. Please refresh the inventory.'
          );
        } else {
          window.alert('Unable to complete sale. Please try again.');
        }
      }
    });
  }

  refreshDashboard(): void {
    // Refresh business performance KPIs
    this.analyticsService.getAnalytics().subscribe({
      next: data => this.analytics.set(data),
      error: err => console.error('Failed to refresh analytics:', err)
    });

    // Refresh category revenue
    this.analyticsService.getCategoryRevenue().subscribe({
      next: data => this.categoryRevenue.set(data),
      error: err => console.error('Failed to refresh category revenue:', err)
    });

    // Refresh inventory risk alerts
    this.analyticsService.getInventoryRisks().subscribe({
      next: data => this.inventoryRisks.set(data),
      error: err => console.error('Failed to refresh risks:', err)
    });

    // Refresh inventory priorities
    this.analyticsService.getInventoryPriorities().subscribe({
      next: data => this.inventoryPriorities.set(data),
      error: err => console.error('Failed to refresh priorities:', err)
    });

    // Refresh product inventory
    this.analyticsService.getProducts().subscribe({
      next: data => this.products.set(data),
      error: err => console.error('Failed to refresh products:', err)
    });
  }

  addNewProduct(): void {
    const product = {
      ...this.newProduct,
      productName: this.newProduct.productName.trim(),
      category: this.newProduct.category.trim()
    };

    // Validate product name and category
    if (!product.productName || !product.category) {
      window.alert('Please enter a product name and category.');
      return;
    }

    // Validate prices
    if (
      !Number.isFinite(product.costPrice) ||
      !Number.isFinite(product.sellingPrice) ||
      product.costPrice < 0 ||
      product.sellingPrice <= 0
    ) {
      window.alert('Please enter valid cost and selling prices.');
      return;
    }

    // Validate stock quantities
    if (
      !Number.isSafeInteger(product.currentStock) ||
      !Number.isSafeInteger(product.reorderLevel) ||
      !Number.isSafeInteger(product.unitsSold) ||
      product.currentStock < 0 ||
      product.reorderLevel < 0 ||
      product.unitsSold < 0
    ) {
      window.alert('Stock, reorder level, and units sold must be non-negative whole numbers.');
      return;
    }

    // Send new product to Spring Boot
    this.analyticsService.addProduct(product).subscribe({
      next: () => {
        window.alert(`${product.productName} added successfully!`);

        // Clear the form
        this.newProduct = {
          productName: '',
          category: '',
          costPrice: 0,
          sellingPrice: 0,
          currentStock: 0,
          reorderLevel: 0,
          unitsSold: 0
        };

        // Refresh dashboard information
        this.refreshDashboard();
      },
      error: (err) => {
        console.error('Failed to add product:', err);
        window.alert('Unable to add product. Please check the details and try again.');
      }
    });
  }

}
