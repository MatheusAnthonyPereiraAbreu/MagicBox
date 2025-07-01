import { Component, OnInit, OnChanges } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RelatoryService, AdHocDTO, Table, Select, ColumnDTO, WhereDTO, GroupByDTO, AggregationDTO, Operator, Aggregation, JoinDTO, JoinType } from './relatory.service';

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
export class RelatoryComponent implements OnInit, OnChanges {
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
    'RANKING_MUSICAS',
    'RANKING_ARTISTAS'
  ];

  // Colunas por tabela
  columnsByTable: { [table: string]: string[] } = {
    ARTISTA: ['id', 'nome', 'ouvintes_globais', 'playcount_globais'],
    ALBUM: ['id', 'nome', 'playcount_globais', 'artista_id'],
    MUSICA: ['id', 'nome', 'duracao_faixa', 'artista_id', 'album_id'],
    ARTISTA_TAG: ['artista_id', 'tag_id'],
    TAG: ['id', 'nome'],
    SIMILARIDADE_ARTISTA: ['artista_base_id', 'artista_similar_id'],
    PAIS: ['id', 'nome', 'codigo'],
    RANKING_MUSICAS: ['musica_id', 'pais_id', 'posicao_ranking', 'data_ultima_atualizacao'],
    RANKING_ARTISTAS: ['artista_id', 'pais_id', 'posicao_ranking', 'data_ultima_atualizacao']
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

  relatoryResult: any = null;
  paginatedResult: any[] = [];
  currentPage: number = 1;
  pageSize: number = 10;
  totalPages: number = 1;
  resultKeys: string[] = [];
  totalItems: number = 0;
  isLoading: boolean = false;

  // Mapa de joins válidos conforme o modelo do banco
  validJoins: { from: string, to: string }[] = [
    { from: 'ALBUM', to: 'ARTISTA' },
    { from: 'MUSICA', to: 'ALBUM' },
    { from: 'MUSICA', to: 'ARTISTA' },
    { from: 'RANKING_ATUAL_MUSICAS_PAISES', to: 'MUSICA' },
    { from: 'RANKING_ATUAL_MUSICAS_PAISES', to: 'PAIS' },
    { from: 'RANKING_ATUAL_ARTISTAS_PAISES', to: 'ARTISTA' },
    { from: 'RANKING_ATUAL_ARTISTAS_PAISES', to: 'PAIS' },
    { from: 'ARTISTA_TAG', to: 'ARTISTA' },
    { from: 'ARTISTA_TAG', to: 'TAG' },
    { from: 'SIMILARIDADE_ARTISTA', to: 'ARTISTA' }
  ];

  constructor(private fb: FormBuilder, private cdr: ChangeDetectorRef, private relatoryService: RelatoryService) {
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

  ngOnInit() {
    // ...
  }

  ngOnChanges() {
    this.updatePagination();
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

  // Função para escolher a tabela principal baseada nas tabelas selecionadas
  selectMainTable(): Table {
    if (this.selectedTables.length === 0) {
      return 'ARTISTA_TAG'; // Tabela padrão
    }

    if (this.selectedTables.length === 1) {
      return this.selectedTables[0].toUpperCase() as Table;
    }

    // Prioriza tabelas principais (que têm mais relacionamentos)
    const tablePriorities = [
      'ARTISTA',    // Tabela central com mais relacionamentos
      'MUSICA',     // Tabela importante
      'ALBUM',      // Tabela importante
      'PAIS',       // Tabela de referência
      'TAG',        // Tabela de referência
      'ARTISTA_TAG', // Tabela de junção
      'RANKING_ATUAL_MUSICAS_PAISES', // Tabela de ranking
      'RANKING_ATUAL_ARTISTAS_PAISES', // Tabela de ranking
      'SIMILARIDADE_ARTISTA' // Tabela de similaridade
    ];

    // Encontra a primeira tabela da lista de prioridades que está selecionada
    for (const priorityTable of tablePriorities) {
      if (this.selectedTables.includes(priorityTable)) {
        return priorityTable as Table;
      }
    }

    // Se não encontrar nenhuma tabela prioritária, retorna a primeira selecionada
    return this.selectedTables[0].toUpperCase() as Table;
  }

  // Função para encontrar o menor caminho de joins válidos conectando todas as tabelas selecionadas
  findJoinPath(): JoinDTO[] {
    if (this.selectedTables.length <= 1) return [];
    // Monta o grafo dos relacionamentos válidos
    const graph: { [key: string]: string[] } = {};
    this.validJoins.forEach(j => {
      if (!graph[j.from]) graph[j.from] = [];
      if (!graph[j.to]) graph[j.to] = [];
      graph[j.from].push(j.to);
      graph[j.to].push(j.from);
    });
    // Algoritmo de busca em largura para encontrar a árvore de conexões
    const tables = this.selectedTables.map(t => t.toUpperCase());
    const visited = new Set<string>();
    const parent: { [key: string]: string | null } = {};
    const queue: string[] = [tables[0]];
    parent[tables[0]] = null;
    while (queue.length > 0) {
      const current = queue.shift()!;
      visited.add(current);
      (graph[current] || []).forEach(neigh => {
        if (!visited.has(neigh) && tables.includes(neigh)) {
          if (!(neigh in parent)) {
            parent[neigh] = current;
            queue.push(neigh);
          }
        }
      });
    }
    // Se nem todas as tabelas foram visitadas, não há caminho
    if (!tables.every(t => visited.has(t))) return [];
    // Monta os joins a partir dos pais
    const joins: JoinDTO[] = [];
    for (const t of tables) {
      const p = parent[t];
      if (p) {
        // Descobre a direção correta do join
        const join = this.validJoins.find(j => (j.from === p && j.to === t) || (j.from === t && j.to === p));
        if (join) {
          joins.push({
            from: join.from,
            to: join.to,
            type: JoinType.INNER
          });
        }
      }
    }
    return joins;
  }

  onSearch() {
    // Monta o DTO AdHocDTO baseado nas seleções
    // Escolhe a tabela principal baseada nas tabelas selecionadas
    const mainTable: Table = this.selectMainTable();
    const joins: JoinDTO[] = this.findJoinPath();

    const columns: ColumnDTO[] = this.selectedColumns.map(col => ({
      table: col.table.toUpperCase(),
      field: col.name.toUpperCase(),
      alias: col.alias
    }));

    const wheres: WhereDTO[] = this.filters.controls
      .filter((f: any) => f.value.field && f.value.value)
      .map((f: any) => {
        const [tableName, fieldName] = f.value.field.split('.');
        return {
          table: tableName.toUpperCase(),
          field: fieldName.toUpperCase(),
          operator: f.value.operator as Operator,
          value: f.value.value
        };
      });

    let groupBy: GroupByDTO | null = null;
    if (this.groups.controls[0]?.value.field) {
      const [tableName, fieldName] = this.groups.controls[0].value.field.split('.');
      const groupColumn: ColumnDTO = {
        table: tableName.toUpperCase(),
        field: fieldName.toUpperCase(),
        alias: this.groups.controls[0].value.alias
      };

      // Por padrão, usa COUNT como agregação
      const aggregation: AggregationDTO = {
        table: tableName.toUpperCase(),
        field: fieldName.toUpperCase(),
        aggregation: Aggregation.COUNT,
        alias: 'count'
      };

      groupBy = {
        columnSet: [groupColumn],
        aggregation: aggregation
      };
    }

    const dto: AdHocDTO = {
      table: mainTable,
      join: joins,
      column: columns,
      where: wheres,
      groupBy: groupBy
    };

    console.log('DTO enviado:', dto);

    this.isLoading = true;
    this.relatoryService.postAdHoc(dto, this.currentPage - 1, this.pageSize).subscribe({
      next: (result) => {
        this.relatoryResult = result && Array.isArray(result.data) ? result.data : [];
        this.totalItems = result && typeof result.total === 'number' ? result.total : 0;
        this.updatePagination();
        this.isLoading = false;
        console.log('Resultado do relatório:', result);
      },
      error: (err) => {
        this.isLoading = false;
        // Trate o erro se quiser
      }
    });
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

  updatePagination() {
    if (!this.relatoryResult || !Array.isArray(this.relatoryResult)) {
      this.paginatedResult = [];
      this.totalPages = 1;
      this.resultKeys = [];
      return;
    }
    this.totalPages = Math.ceil(this.totalItems / this.pageSize) || 1;
    this.paginatedResult = this.relatoryResult;
    this.resultKeys = this.paginatedResult.length > 0 ? Object.keys(this.paginatedResult[0]) : [];
  }

  goToPage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.onSearch();
  }

  set pageSizeSetter(val: number) {
    this.pageSize = val;
    this.currentPage = 1;
    this.updatePagination();
  }

  downloadJson() {
    if (!this.relatoryResult) return;
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.relatoryResult, null, 2));
    const downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", "relatorio.json");
    document.body.appendChild(downloadAnchorNode);
    downloadAnchorNode.click();
    downloadAnchorNode.remove();
  }
}
