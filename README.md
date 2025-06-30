# 🎵 MagicBox

Um dashboard musical moderno e interativo para centralização e análise de dados musicais em tempo real.

## 📋 Sobre o Projeto

O **MagicBox** é uma aplicação web desenvolvida em Angular que permite acompanhar as principais tendências musicais, artistas, músicas, gêneros e estatísticas do universo musical em diferentes regiões do mundo. 

### ✨ Funcionalidades

- **Dashboard Interativo**: Visualização de dados musicais através de gráficos dinâmicos
- **Relatórios Avançados**: Geração de relatórios com filtros por data e categoria
- **Análise de Tendências**: Acompanhamento de artistas, músicas e álbuns mais populares
- **Interface Moderna**: Design responsivo com glassmorphism e tema escuro
- **Dados em Tempo Real**: Integração com APIs para dados atualizados
- **Exportação de Dados**: Funcionalidade para exportar relatórios

## 🛠️ Tecnologias Utilizadas

- **Frontend**: Angular 19
- **Estilização**: Tailwind CSS
- **Componentes UI**: PrimeNG
- **Gráficos**: ngx-charts
- **Ícones**: Phosphor Icons
- **Build Tool**: Angular CLI

## 📋 Pré-requisitos

Antes de começar, você precisa ter instalado no seu computador:

- **Node.js** (versão 18 ou superior)
- **npm** (gerenciador de pacotes do Node.js)

### Como instalar o Node.js

1. Acesse [nodejs.org](https://nodejs.org/)
2. Baixe a versão LTS (Long Term Support)
3. Execute o instalador e siga as instruções
4. Verifique a instalação abrindo o terminal e digitando:
   ```bash
   node --version
   npm --version
   ```

## 🚀 Como Instalar

### 1. Clone o repositório

```bash
git clone https://github.com/MatheusAnthonyPereiraAbreu/MagicBox
cd magicbox
```

### 2. Instale as dependências

```bash
npm install
```

### 3. Execute o projeto

```bash
npm start
```

O projeto estará disponível em `http://localhost:4200`

## 📖 Como Usar

### Navegação

O MagicBox possui uma interface intuitiva com as seguintes seções:

- **🏠 Home**: Dashboard principal com visão geral dos dados musicais
- **📊 Relatórios**: Geração e visualização de relatórios detalhados
- **ℹ️ Sobre**: Informações sobre o projeto e criadores

### Funcionalidades Principais

#### Dashboard (Home)
- Visualização de gráficos de pizza para artistas, músicas e álbuns
- Gráfico de área para análise de gêneros musicais
- Carrossel de bandeiras de países
- Dados atualizados em tempo real

#### Relatórios
- Filtros por data inicial e final
- Seleção de categorias
- Carrossel de gráficos (pizza, barras, linha, área)
- Funcionalidade de exportação

#### Sobre
- Informações sobre o projeto
- Perfis dos criadores com dados do GitHub
- Links para redes sociais

## 🔧 Scripts Disponíveis

```bash
# Iniciar servidor de desenvolvimento
npm start

# Construir para produção
npm run build

# Executar testes
npm test

# Construir em modo watch
npm run watch
```

## 📁 Estrutura do Projeto

```
MagicBox/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── sidebar/
│   │   ├── pages/
│   │   │   ├── about/
│   │   │   ├── edit/
│   │   │   ├── home/
│   │   │   └── relatory/
│   │   └── shared/
│   ├── assets/
│   │   └── icons/
│   └── styles/
├── public/
└── package.json
```


## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request


## 👥 Criadores

- **Contribuidores**: [Augusto Zanoli, Alejandro de Paiva, Lucas Pinheiro, Matheus Anthony, Tomaz Lávez]


**MagicBox** - Transformando dados musicais em insights visuais 🎵
