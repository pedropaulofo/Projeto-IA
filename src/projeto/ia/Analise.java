package projeto.ia;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpl7.Query;

public class Analise {
	private String frase = null;
	private TipoCrase tipo;
	private String textoAnterior = "";
	private String palavraPrecedente = "";
	private String textoPosterior = "";
	private String palavraSucessora = "";
        private Boolean precedenteProibido = false;
        private Boolean sucessorProibido = false;
        private String dica = "";

        /*
	public static void main(String[] args){
            System.out.println("Main:");
            Analise anal;
            try {
                String texto = ".";
                anal = new Analise(texto);
                Boolean resultado = anal.analisar();
                if(resultado) System.out.println("Caso proibitivo");
                else System.out.println("Crase OK");
                System.out.println(anal.dica);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
          
        }*/
	
	public Analise(String textoInput) throws Exception {
		frase = textoInput;
		tipo = null;

	}

	private Boolean analisar() throws Exception {

		if (frase == null || frase.equals("")) {
			throw new Exception("Frase vazia.");
		}
		identificarPartes();
                precedenteProibido = consultaProibicaoPrecedente();
                sucessorProibido = consultaProibicaoSucessor();
                
                // retorna TRUE se um dos casos identificar uso proibido da crase
                // retorna FALSE se nao for identificada irregularidade no uso da crase.
		return precedenteProibido || sucessorProibido;
	}
        
        private Boolean consultaProibicaoPrecedente(){
            if (palavraPrecedente.isEmpty()) return false;
            Query abrirArquivo = new Query("consult('caso_precedente.pl')");
            abrirArquivo.hasSolution();
            
            //checar se a palavra precedente se enquadra num dos casos proibitivos do uso da crase
            Query checarPrecedente = new Query("pro(" + palavraPrecedente.toLowerCase() + ")");
	    if (checarPrecedente.hasSolution()) {
		dica += "A crase não pode ser precedida por palavras como '" + palavraPrecedente + "'. ";
                
                // retorna verdadeiro se o precedente torna o uso proibido
                return true;
	    }
            abrirArquivo.close();
            
            // retorna falso se o precedente não implica na proibicao da crase
            return false;
        }
        
        private Boolean consultaProibicaoSucessor(){
            if (palavraSucessora.isEmpty()) return false;
            
            System.out.println(palavraSucessora);
            Query abrirArquivo;
            abrirArquivo = new Query("consult('caso_precedente.pl')");
            abrirArquivo.hasSolution();
            Query checarPrepConj = new Query("pro(" + palavraSucessora.toLowerCase() + ")");
	    if (checarPrepConj.hasSolution()) {
		dica += "A crase não pode ser sucedida por palavras como'" + palavraSucessora + "'. ";
                return true;
            }
            abrirArquivo.close();
            checarPrepConj.close();
            
            //casos proibitivos gerais do sucessor para plural ou singular
            abrirArquivo = new Query("consult('caso_geral.pl')");
            boolean ok = abrirArquivo.hasSolution();
            Query checarCasoGeral = new Query("p(" + palavraSucessora.toLowerCase() + ")");
	    if (checarCasoGeral.hasSolution()) {
		dica += "A crase não pode ser sucedida por palavras como'" + palavraSucessora + "' (artigos, adverbios, pronomes não-possessivos. ";
                return true;
            }
            abrirArquivo.close();
            checarCasoGeral.close();
            
            //abrir o arquvivo correspondente se é plural ou singular
            if (tipo == TipoCrase.A){
                abrirArquivo = new Query("consult('caso_singular.pl')");
            }
            else if (tipo == TipoCrase.AS){
                abrirArquivo = new Query("consult('caso_plural.pl')");
            }
            ok = abrirArquivo.hasSolution();
            
            //checar de acordo com as regras do caso plural ou singular
            Query checarSucessor = new Query("proibida(" + palavraSucessora.toLowerCase() + ")");
	    if (checarSucessor.hasSolution()) {
		dica += "A crase neste caso não pode ser sucedida por '" + palavraSucessora + "'.";
                return true;
	    }
            abrirArquivo.close();
            
            return false;
        }

	private String identificarPartes() throws Exception {
		String[] palavras = removePontuacao(frase).split(" ");
		int posCrase = -1;
                
		for (int i = 0; i < palavras.length; i++) {
			if (palavras[i].toLowerCase().equals("à")) {
				if (posCrase >= 0)
					throw new Exception("A frase contem mais de uma crase. Digite uma oracao de cada vez.");
				posCrase = i;
				tipo = TipoCrase.A;
				if (i > 0)
					palavraPrecedente = palavras[i - 1];
				if (i < palavras.length - 1)
					palavraSucessora = palavras[i + 1];
			} else if (palavras[i].toLowerCase().equals("às")) {
				if (posCrase >= 0)
					throw new Exception("A frase contem mais de uma crase. Digite uma oracao de cada vez.");
				posCrase = i;
				tipo = TipoCrase.AS;
				if (i > 0)
					palavraPrecedente = palavras[i - 1];
				if (i < palavras.length - 1)
					palavraSucessora = palavras[i + 1];
			} else if (posCrase < 0)
				textoAnterior = textoAnterior + palavras[i] + " ";
			else
				textoPosterior = textoPosterior + " " + palavras[i];
		}

		if (posCrase < 0)
			throw new Exception("A frase dada nao contem uso de crase.");

		return String.join(" ", palavras);
	}

	private String removePontuacao(final String input) {
		final StringBuilder builder = new StringBuilder();
                
		for (final char c : input.toCharArray())
			if (Character.isLetterOrDigit(c) || c == ' ' || isVogalEspecial(c))
				builder.append(c);
		return builder.toString();
	}
        
        private Boolean isVogalEspecial(char c){
            char[] especiais = new char[]{'à','á','â','é','ê','í','ó','ô','ú','ª','º'};
            for(int i = 0; i < especiais.length; i++)
                if(especiais[i] == c) return true;
            return false;
        }

	public TipoCrase getTipo() {
		return tipo;
	}

	public String getFrase() {
		return frase;
	}

	public String getTextoAnterior() {
		return textoAnterior;
	}

	public String getTextoPosterior() {
		return textoPosterior;
	}

    
    public Boolean getPrecedenteProibido() {
        return precedenteProibido;
    }

        public Boolean getSucessorProibido() {
        return sucessorProibido;
    }
        
    public String getDica(){
        return dica;
    }

}
