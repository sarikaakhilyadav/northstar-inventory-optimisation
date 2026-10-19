import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface InventoryAnalytics {
    totalProducts: number;
    totalRevenue: number;
    totalInventoryCost: number;
    totalPotentialProfit: number;
    productsRequiringReorder: number;
    slowMovingProducts: number;
}

@Injectable({
    providedIn: 'root'
})
export class InventoryAnalyticsService {
    private readonly http = inject(HttpClient);

    private readonly apiUrl = '/api/analytics';
    getAnalytics(): Observable<InventoryAnalytics> {
        return this.http.get<InventoryAnalytics>(this.apiUrl);
    }
    getCategoryRevenue(): Observable<CategoryRevenue[]> {
        return this.http.get<CategoryRevenue[]>(
            '/api/analytics/category-revenue'
        );
    }
    getInventoryRisks(): Observable<InventoryRisk[]> {
        return this.http.get<InventoryRisk[]>(
            '/api/analytics/inventory-risks'
        );
    }
    getInventoryPriorities(): Observable<InventoryPriority[]> {
        return this.http.get<InventoryPriority[]>(
            '/api/analytics/inventory-priorities'
        );
    }
    getProducts(): Observable<Product[]> {
        return this.http.get<Product[]>('/api/products');
    }

    restockProduct(
        productId: number,
        quantity: number
    ): Observable<Product> {
        return this.http.patch<Product>(
            `/api/products/${productId}/restock`,
            { quantity }
        );
    }

    sellProduct(
        productId: number,
        quantity: number
    ): Observable<Product> {
        return this.http.patch<Product>(
            `/api/products/${productId}/sell`,
            { quantity }
        );
    }

    addProduct(product: Omit<Product, 'productId'>): Observable<Product> {
        return this.http.post<Product>(
            '/api/products',
            product
        );
    }

}
export interface CategoryRevenue {
    category: string;
    revenue: number;
}

export interface InventoryRisk {
    productId: number;
    productName: string;
    category: string;
    currentStock: number;
    reorderLevel: number;
    unitsSold: number;
    riskStatus: string;
    riskExplanation: string;
    recommendation: string;
}

export interface InventoryPriority {
    productId: number;
    productName: string;
    category: string;
    currentStock: number;
    reorderLevel: number;
    unitsSold: number;
    priority: string;
    reason: string;
}

export interface Product {
    productId: number;
    productName: string;
    category: string;
    costPrice: number;
    sellingPrice: number;
    currentStock: number;
    reorderLevel: number;
    unitsSold: number;
}
