Toy è un linguaggio di programmazione che si ispira ad un linguaggio omonimo e più vasto creato per un corso di Compilatori dell'Università di Salerno.

In questo progetto vengono percorse tutte le fasi frontend di un compilatore per convertire un sorgente Toy in una forma intermedia realizzata in C.

Ai moduli implementati si aggancia, poi, il compilatore Clang per mandare in esecuzione il sorgente C generato.

Per eseguire correttamente il compilatore su una macchina Linux o Windows è necessario aprire il progetto con una versione di IntelliJ inferiore alla 2020.3 e avere installato sul computer i pacchetti JFlex e Java CUP (nella cartella C per Windows e in quella del progetto per Linux). Tra questi, è necessario importare Java CUP nel seguente modo:
"File -> Project Structure -> Module -> Dependencies -> + -> carica il JAR Java Cup Runtime dalla cartella dedicata".

Ai fini di generare un file C indentato correttamente, è necessario installare il plugin Artistic Style, reperibile nel sito dedicato: http://astyle.sourceforge.net/.

Per creare manualmente un eseguibile è possibile utilizzare gli script dedicati denominati come Generate.bat e Compile.sh rispettivamente per Windows e Linux.
Il programma, però, permette di chiamare tali script in automatico, quindi esegue in automatico il programma Toy fornito al compilatore (su Linux ciò funziona solo tramite shell XTerm).

> [Visualizza la specifica lessicale e grammaticale](https://github.com/frekkanzer2/SimpleToyCompiler/blob/main/documentation/Specifica%20Lessicale%20e%20Sintattica.pdf)

> [Visualizza la documentazione semantica e generazione del codice](https://github.com/frekkanzer2/SimpleToyCompiler/blob/main/documentation/Documentazione%20Progetto.pdf)
