import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Column {
  table: string;
  name: string;
  alias: string;
  selected: boolean;
}

interface Filter {
  field: string;
  operator: string;
  value: string;
}

interface Group {
  field: string;
  alias: string;
}

@Component({
  selector: 'app-relatory',
  templateUrl: './relatory.component.html',
  styleUrls: ['./relatory.component.scss'],
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
})
export class RelatoryComponent {
  colorScheme = 'cool';

  pieData = [
    { name: 'Categoria 1', value: 35 },
    { name: 'Categoria 2', value: 55 },
    { name: 'Categoria 3', value: 10 }
  ];

  // Gráfico de Barras
  barData = [
    { name: 'Janeiro', value: 30 },
    { name: 'Fevereiro', value: 80 },
    { name: 'Março', value: 45 },
    { name: 'Abril', value: 60 }
  ];

  // Gráfico de Linha
  lineData = [
    {
      name: 'Vendas',
      series: [
        { name: '2021', value: 50 },
        { name: '2022', value: 80 },
        { name: '2023', value: 65 }
      ]
    }
  ];

  // Gráfico de Área
  areaData = [
    {
      name: 'Usuários',
      series: [
        { name: 'Jan', value: 20 },
        { name: 'Fev', value: 40 },
        { name: 'Mar', value: 35 },
        { name: 'Abr', value: 60 }
      ]
    }
  ];

  view: [number, number] = [800, 600];

  selectedTab = 0;

  // Tabelas disponíveis
  tables: string[] = [
    'ARTISTA',
    'ALBUM',
    'MUSICA',
    'TAG',
    'ARTISTA_TAG',
    'SIMILARIDADE_ARTISTA',
    'PAIS',
    'RANKING_ATUAL_MUSICAS_PAISES',
    'RANKING_ATUAL_ARTISTAS_PAISES'
  ];

  // Colunas por tabela
  columnsByTable: { [table: string]: string[] } = {
    ARTISTA: ['id', 'nome', 'ouvintes_globais', 'playcount_globais'],
    ALBUM: ['id', 'nome', 'playcount_globais', 'artista_id'],
    MUSICA: ['id', 'nome', 'duracao_faixa', 'artista_id', 'album_id'],
    TAG: ['id', 'name'],
    ARTISTA_TAG: ['artista_id', 'tag_id'],
    SIMILARIDADE_ARTISTA: ['artista_base_id', 'artista_similar_id'],
    PAIS: ['id', 'nome', 'codigo'],
    RANKING_ATUAL_MUSICAS_PAISES: ['musica_id', 'pais_id', 'posicao_ranking', 'data_ultima_atualizacao'],
    RANKING_ATUAL_ARTISTAS_PAISES: ['artista_id', 'pais_id', 'posicao_ranking', 'data_ultima_atualizacao']
  };

  // Tabelas selecionadas
  selectedTables: string[] = [];

  // Tabelas que estão sendo removidas (para animação)
  removingTables: string[] = [];

  // Colunas selecionadas (apenas das tabelas selecionadas)
  selectedColumns: Column[] = [];

  checkedColumns: { [key: string]: boolean } = {};
  checkedTables: { [key: string]: boolean } = {};

  formTables: FormGroup;
  formColumns: FormGroup;
  formAliases: FormGroup;
  form: FormGroup;

  constructor(private fb: FormBuilder, private cdr: ChangeDetectorRef) {
    this.formTables = this.fb.group({});
    this.formColumns = this.fb.group({});
    this.formAliases = this.fb.group({});
    this.form = this.fb.group({
      filters: this.fb.array([this.createFilterGroup()]),
      groups: this.fb.array([this.createGroupGroup()])
    });
    this.tables.forEach(table => {
      this.formTables.addControl(table, new FormControl(false));
    });
    this.updateFormColumns();
    this.formTables.valueChanges.subscribe(val => {
      const newSelectedTables = this.tables.filter(t => val[t]);
      const removedTables = this.selectedTables.filter(t => !newSelectedTables.includes(t));
      if (removedTables.length > 0) {
        this.removingTables = [...this.removingTables, ...removedTables];
        setTimeout(() => {
          this.selectedTables = newSelectedTables;
          this.removingTables = this.removingTables.filter(t => !removedTables.includes(t));
          this.updateFormColumns();
          this.cdr.detectChanges();
        }, 400);
      } else {
        this.selectedTables = newSelectedTables;
        this.updateFormColumns();
      }
    });
    this.formColumns.valueChanges.subscribe(val => {
      this.selectedColumns = Object.keys(val)
        .filter(key => val[key])
        .map(key => {
          const [table, name] = key.split('.');
          return { table, name, alias: this.formAliases.get(key)?.value || '', selected: true };
        });
      this.cdr.detectChanges();
    });
  }

  // Reactive Forms para filtros e agrupamentos
  get filters(): FormArray {
    return this.form.get('filters') as FormArray;
  }
  get groups(): FormArray {
    return this.form.get('groups') as FormArray;
  }
  createFilterGroup(): FormGroup {
    return this.fb.group({
      field: [''],
      operator: ['='],
      value: ['']
    });
  }
  createGroupGroup(): FormGroup {
    return this.fb.group({
      field: [''],
      alias: ['']
    });
  }
  addFilter() {
    this.filters.push(this.createFilterGroup());
  }
  removeFilter(index: number) {
    if (this.filters.length > 1) {
      this.filters.removeAt(index);
    } else {
      this.filters.at(0).reset({ field: '', operator: '=', value: '' });
    }
  }
  addGroup() {
    this.groups.push(this.createGroupGroup());
  }
  removeGroup(index: number) {
    if (this.groups.length > 1) {
      this.groups.removeAt(index);
    } else {
      this.groups.at(0).reset({ field: '', alias: '' });
    }
  }

  // Atualiza o formColumns conforme as tabelas selecionadas
  updateFormColumns() {
    const currentCols = Object.keys(this.formColumns.controls);
    const neededCols: string[] = [];
    for (const table of this.selectedTables) {
      for (const col of this.columnsByTable[table] || []) {
        neededCols.push(table + '.' + col);
        if (!this.formColumns.contains(table + '.' + col)) {
          this.formColumns.addControl(table + '.' + col, new FormControl(false));
        }
      }
    }
    // Remove controles de colunas não mais necessárias
    for (const c of currentCols) {
      if (!neededCols.includes(c)) {
        this.formColumns.removeControl(c);
      }
    }
    // Atualiza selectedColumns
    this.selectedColumns = neededCols
      .filter(key => this.formColumns.get(key)?.value)
      .map(key => {
        const [table, name] = key.split('.');
        return { table, name, alias: this.formAliases.get(key)?.value || '', selected: true };
      });
    this.updateFormAliases();
  }

  updateFormAliases() {
    const neededCols: string[] = [];
    for (const table of this.selectedTables) {
      for (const col of this.columnsByTable[table] || []) {
        neededCols.push(table + '.' + col);
        if (!this.formAliases.contains(table + '.' + col)) {
          this.formAliases.addControl(table + '.' + col, new FormControl(''));
        }
      }
    }
    // Remove controles não necessários
    for (const c of Object.keys(this.formAliases.controls)) {
      if (!neededCols.includes(c)) {
        this.formAliases.removeControl(c);
      }
    }
  }

  // Métodos utilitários para o template
  isTableSelected(table: string): boolean {
    return this.formTables.get(table)?.value;
  }

  onTableCheckboxChange(table: string, checked: boolean) {
    this.formTables.get(table)?.setValue(checked);
  }

  isColumnSelected(col: any): boolean {
    return this.formColumns.get(col.table + '.' + col.name)?.value;
  }

  onColumnCheckboxChange(col: any, checked: boolean) {
    this.formColumns.get(col.table + '.' + col.name)?.setValue(checked);
    this.updateFormColumns();
    this.cdr.detectChanges();
  }

  // Retorna todas as colunas disponíveis das tabelas selecionadas
  get availableColumns(): any[] {
    const cols: any[] = [];
    for (const table of this.selectedTables) {
      for (const col of this.columnsByTable[table] || []) {
        const isChecked = this.selectedColumns.some(c => c.table === table && c.name === col);
        cols.push({ table, name: col, alias: '', selected: false, _checked: isChecked });
      }
    }
    return cols;
  }

  // Retorna as colunas de uma tabela específica
  getColumnsForTable(tableName: string): string[] {
    return this.columnsByTable[tableName] || [];
  }

  // Retorna todas as tabelas para exibição (selecionadas + em remoção)
  get allDisplayTables(): string[] {
    return [...this.selectedTables, ...this.removingTables];
  }

  // Adiciona coluna à seleção
  addColumn(table: string, name: string) {
    if (!this.selectedColumns.find(c => c.table === table && c.name === name)) {
      this.selectedColumns.push({ table, name, alias: '', selected: true });
    }
  }

  removeColumn(table: string, name: string) {
    this.selectedColumns = this.selectedColumns.filter(c => !(c.table === table && c.name === name));
  }

  onSearch() {
    // Monta a query baseada nas seleções
    console.log('Tabelas:', this.selectedTables);
    console.log('Colunas:', this.selectedColumns);
    console.log('Filtros:', this.filters);
    console.log('Agrupamentos:', this.groups);
    // TODO: Implementar chamada ao backend
  }

  onReset() {
    this.selectedTables = [];
    this.selectedColumns = [];
    this.checkedColumns = {};
    this.checkedTables = {};
    this.tables.forEach(table => this.formTables.get(table)?.setValue(false));
    Object.keys(this.formColumns.controls).forEach(c => this.formColumns.get(c)?.setValue(false));
    Object.keys(this.formAliases.controls).forEach(c => this.formAliases.get(c)?.setValue(''));
    // Resetar filtros e grupos (FormArrays)
    while (this.filters.length > 1) this.filters.removeAt(0);
    this.filters.at(0).reset({ field: '', operator: '=', value: '' });
    while (this.groups.length > 1) this.groups.removeAt(0);
    this.groups.at(0).reset({ field: '', alias: '' });
  }
}
