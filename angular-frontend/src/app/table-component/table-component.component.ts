import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, combineLatest, debounceTime } from 'rxjs';

interface CombinedData {
  patientId: number;
  addressId: number;
  prename: string;
  surname: string;
  birthdate: string;
  street: string;
  city: string;
  postalCode: string;
}

interface Page {
  pageIndex: number;
  pageSize: number;
}

interface PaginatedResponse<T> {
  pages: number;
  data: T[];
}

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.sass'],
})
export class TableComponentComponent {
  dataSource = new MatTableDataSource<CombinedData>([]);
  displayedColumns: string[] = [
    'patientId',
    'addressId',
    'prename',
    'surname',
    'birthdate',
    'street',
    'city',
    'postalCode',
  ];

  search$ = new BehaviorSubject<string>('');
  sort$ = new BehaviorSubject<Sort>({ active: 'addressId', direction: 'asc' });
  page$ = new BehaviorSubject<Page>({ pageIndex: 0, pageSize: 10 });
  nrOfObjectsMatchingSearch$ = new BehaviorSubject<number>(0);

  constructor(http: HttpClient) {
    combineLatest({
      search: this.search$.pipe(debounceTime(500)),
      sort: this.sort$,
      page: this.page$,
    }).subscribe(({ search, sort, page }) => {
      http
        .get<PaginatedResponse<CombinedData>>(
          `${
            (window as any)._env_?.BACKEND_URL ?? 'http://localhost:8080'
          }/data?pageNumber=${page.pageIndex}&pageSize=${page.pageSize}${
            sort.active === '' || sort.direction === ''
              ? ''
              : `&sortField=${sort.active}&sortDescending=${
                  sort.direction === 'desc' ? 'true' : 'false'
                }`
          }${search === '' ? '' : `&search=${search}`}`
        )
        .forEach((res: PaginatedResponse<CombinedData>) => {
          this.nrOfObjectsMatchingSearch$.next(res.pages * page.pageSize);
          this.dataSource.data = res.data;
        });
    });
  }

  applyFilter(value: string): void {
    this.dataSource.filter = value.trim().toLowerCase();
  }

  handleSearchInput(e: Event) {
    this.search$.next((e.target as HTMLInputElement).value);
  }

  handlePageEvent(e: PageEvent) {
    this.page$.next({
      pageSize: e.pageSize,
      pageIndex: e.pageIndex,
    });
  }

  handleSortData(sort: Sort) {
    this.sort$.next(sort);
  }
}
