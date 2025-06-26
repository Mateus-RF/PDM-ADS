🌱 App de Monitoramento de Árvores Nativas

Este aplicativo permite cadastrar espécies de árvores com localização, estado de conservação, data de plantio e imagem capturada pela câmera do dispositivo. Utiliza banco de dados local SQLite com Room.

📦 Funcionalidades:

    -📸 Captura de imagem via câmera

    -🌍 Registro de latitude e longitude

    -🧾 Cadastro de espécie, data e estado de conservação

    -🔁 Edição e atualização dos registros

    -🗂️ Visualização em cards com imagem

    -🧠 Banco de dados local com Room (SQLite)

Pré-requisitos:
    -Android Studio Giraffe ou mais recente
    -SDK do Android 24 (mínimo) até 35 (target)
    -Dispositivo ou emulador com câmera (para testes com imagem)

Estrutura de Telas:
    -MainHomeActivity → Lista de árvores em cards
    -MainActivityAddEspecie → Cadastro ou edição de árvore
    -DetalheArvoreActivity → Tela de detalhe de uma árvore
    -RelatorioActivity → Exibe um relatório de acordo com o estado de conservação

🚀 Como rodar o projeto:
    1.Clone ou importe o projeto no Android Studio

    2.Execute em um emulador ou dispositivo real com câmera

    3.Faça o primeiro cadastro clicando em "Adicionar espécie"

    4.Use o botão de câmera para capturar a imagem da árvore

    4.Edite registros, visualize detalhes e veja os relatorios gerados para cada tipo de condição.