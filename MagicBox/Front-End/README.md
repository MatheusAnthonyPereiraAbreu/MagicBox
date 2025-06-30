# 🎵 MagicBox

Um dashboard musical moderno e interativo para centralização e análise de dados musicais em tempo real.

## 📋 Sobre o Projeto

O **MagicBox** é uma aplicação web desenvolvida em Angular que permite acompanhar as principais tendências musicais, artistas, músicas, gêneros e estatísticas do universo musical em diferentes regiões do mundo. 

### ✨ Funcionalidades

- **🎬 Splash Screen**: Tela de introdução elegante com logo animado
- **🎨 Sistema de Temas**: 6 temas dinâmicos (Roxo, Azul, Verde, Âmbar, Rosa, Índigo)
- **🎯 Logo Dinâmico**: Logo que muda de cor conforme o tema escolhido
- **📊 Dashboard Interativo**: Visualização de dados musicais através de gráficos dinâmicos
- **📈 Relatórios Ad Hoc**: Geração de relatórios sob demanda com filtros personalizados
- **📈 Análise de Tendências**: Acompanhamento de artistas, músicas e álbuns mais populares
- **💎 Interface Moderna**: Design responsivo com glassmorphism e tema escuro
- **⚡ Dados em Tempo Real**: Integração com APIs para dados atualizados
- **📤 Exportação de Dados**: Funcionalidade para exportar relatórios
- **📱 Design Responsivo**: Interface adaptável para todos os dispositivos

## 🎨 Sistema de Temas

O MagicBox oferece 6 temas dinâmicos que podem ser alterados na página de edição:

- **🟣 Roxo** (padrão): `#8B5CF6`
- **🔵 Azul**: `#3B82F6`
- **🟢 Verde**: `#10B981`
- **🟡 Âmbar**: `#F59E0B`
- **🔴 Rosa**: `#F43F5E`
- **🟦 Índigo**: `#6366F1`

### Como Alterar o Tema
1. Acesse a página **Edit** no menu lateral
2. Clique em uma das cores disponíveis
3. O tema será aplicado instantaneamente em toda a aplicação
4. O logo também muda de cor automaticamente

## 🎬 Splash Screen

Ao acessar o MagicBox, você verá uma tela de introdução elegante que:
- **⏱️ Dura 3 segundos** com animações suaves
- **🎯 Mostra o logo** com efeito de flutuação
- **📝 Exibe o nome** "MagicBox" com gradiente dinâmico
- **📱 É totalmente responsiva** para mobile e desktop
- **🎨 Responde ao tema** escolhido pelo usuário

## 🛠️ Tecnologias Utilizadas

- **Frontend**: Angular 19
- **Estilização**: Tailwind CSS
- **Componentes UI**: PrimeNG
- **Gráficos**: ngx-charts
- **Ícones**: Phosphor Icons
- **Build Tool**: Angular CLI
- **Animações**: CSS3 Animations & Transitions

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
cd MagicBox/Front-End
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
- **📊 Relatórios Ad Hoc**: Geração e visualização de relatórios sob demanda
- **⚙️ Edit**: Personalização de temas e configurações
- **ℹ️ Sobre**: Informações sobre o projeto e criadores

### Funcionalidades Principais

#### 🎬 Splash Screen
- Tela de introdução com logo animado
- Duração de 3 segundos
- Transições suaves para o conteúdo principal

#### 🎨 Sistema de Temas (Edit)
- 6 temas dinâmicos disponíveis
- Mudança instantânea de cores
- Logo que responde ao tema escolhido
- Interface consistente em toda a aplicação

#### Dashboard (Home)
- Visualização de gráficos de pizza para artistas, músicas e álbuns
- Gráfico de área para análise de gêneros musicais
- Carrossel de bandeiras de países
- Dados atualizados em tempo real
- Logo dinâmico no cabeçalho

#### Relatórios Ad Hoc
- Seleção de categorias
- Geração sob demanda

#### Sobre
- Informações sobre o projeto
- Perfis dos criadores com dados do GitHub
- Links para redes sociais
- Logo dinâmico no cabeçalho

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
MagicBox/Front-End/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── logo/                 # Logo dinâmico
│   │   │   ├── sidebar/              # Menu lateral
│   │   │   └── splash-screen/        # Tela de introdução
│   │   ├── pages/
│   │   │   ├── about/                # Página sobre
│   │   │   ├── edit/                 # Configuração de temas
│   │   │   ├── home/                 # Dashboard principal
│   │   │   └── relatory/             # Relatórios Ad Hoc
│   │   └── shared/
│   ├── assets/
│   │   └── icones/
│   │       ├── magicbox.svg          # Logo original
│   │       └── magicbox-theme.svg    # Logo com tema dinâmico
│   └── styles/
│       └── styles.scss               # Estilos globais e temas
├── public/
└── package.json
```

## 🎨 Componentes Principais


### SplashScreenComponent
- Tela de introdução com animações
- Logo flutuante e título com gradiente
- Responsivo para mobile e desktop

### EditComponent
- Interface para seleção de temas
- 6 opções de cores disponíveis
- Aplicação instantânea de mudanças
- Eventos para comunicação entre componentes

## 🚀 Funcionalidades Avançadas

### Sistema de Eventos
- Eventos customizados para comunicação entre componentes
- Detecção automática de mudanças de tema
- Observação de mudanças nas classes do body
- Transições suaves entre estados

### Responsividade
- Design adaptável para todos os dispositivos
- Breakpoints otimizados para mobile, tablet e desktop
- Componentes que se ajustam automaticamente
- Experiência consistente em todas as telas

### Performance
- Animações CSS otimizadas
- Lazy loading de componentes
- Transições suaves e eficientes
- Carregamento rápido da aplicação

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 👥 Criadores

- **Contribuidores**: [Augusto Zanoli, Alejandro de Paiva, Lucas Pinheiro, Matheus Anthony, Tomaz Lávez]

**MagicBox** - Transformando dados musicais em insights visuais 🎵✨
