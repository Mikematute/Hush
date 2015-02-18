 /*
 * Juego LA Nena
 *
 *
 * @author William Miguel Rosado Aíza
 * A00815329
 * @version Beta 1.3
 * @date 11/02/2015
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.net.URL;


/**
 *
 * @author AntonioM
 */
public class Juego extends Applet implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basNena;         // Objeto principal
    private Base basMalo;         // Objeto malo
    private LinkedList<Base> lklFantasmas; //Guarda a todos los Fantasmas
    private LinkedList<Base> lklJuanitos; //Guarda a todos los Fantasmas
    private int iDireccion;             //Direccion de Nena
    private int iPosX;                  //Poscicion en x
    private int iPosY;                  //Poscicion en Y
    private boolean bP;                 //Boleana pausa
    private boolean bEsc;               //booleana para temrinar
    private int iVidas;                 //contador de Vidas
    private int iCrashes;               //contador de crshes con juanitos
    private Image imaGameOver; // Imagen para GameOver
    private int iVelocidad;         //Velocidad de los Juanitos
    private int iPuntos;             //Puntuacion

    

    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip aucSonidoNena1;   // Objeto sonido de nena
    private AudioClip aucSonidoNena2;   // Objeto sonido de Nena

	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
        
        //puntos se inicioan en 0
        iPuntos=0;
        
        //iniializo x y y 
        iPosX = 0;
        iPosY = 0;
        
        //inicializacion de Vidas
        iVidas=(int)(Math.random() * 2) + 3;
        
        //inicioalizacion de crahses
        iCrashes=0;
        
        //inicializo iVelocidad
        iVelocidad=1;
        
        //Inicializo las booleanas en sus respectivas para que no crashee
        bP=false;
        bEsc=false;
        
        //Inicializacion de la lista de fantasmas y juanitos
        lklFantasmas = new LinkedList <Base>();
        
        lklJuanitos = new LinkedList <Base>();
        
        //Inicializacion de iDireccion
        iDireccion=0;


             
	URL urlImagenPrincipal = this.getClass().getResource("chimpy.gif");
                
        // se crea el objeto para principal 
	basNena = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
        basNena.setX((getWidth() / 2)-basNena.getAncho()/2);
        basNena.setY((getHeight() / 2)-basNena.getAlto()/2);
        
        // defino la imagen del Fantasma
	URL urlImagenMalo = this.getClass().getResource("fantasmita.gif");
        
        int iRandom = (int)(Math.random() * 2) + 8;
        
        //creo los fantasmas
        for(int i = 0; i < iRandom; i++){
            //creo otro objeto Base
            iPosX = (int) (Math.random() *(getWidth() / 4));    
            iPosY = (int) (Math.random() *(getHeight() / 4));
            basMalo = new Base(iPosX,iPosY,getWidth() / iMAXANCHO,
                    getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
            lklFantasmas.add(basMalo);
        }
        //reposciciono a los fantasmas afuera del applet a la izqueirda
        for(Base basMalo : lklFantasmas){
            basMalo.setX((int) (-1 * Math.random() *(getWidth() - 
                    basMalo.getAncho())));
            basMalo.setY((int) (Math.random() *(getHeight() - 
                    basMalo.getAlto())));
        }
        
        URL urlImagenMalo2 = this.getClass().getResource("juanito.gif");
        
        iRandom = (int)(Math.random() * 5) + 10;
        
        //creo los Juanitos
        for(int i = 0; i < iRandom; i++){
            //creo otro objeto Base
            iPosX = (int) (Math.random() *(getWidth() / 4));    
            iPosY = (int) (Math.random() *(getHeight() / 4));
            basMalo = new Base(iPosX,iPosY,getWidth() / iMAXANCHO,
                    getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenMalo2));
            lklJuanitos.add(basMalo);
        }
        //reposciciono a los Juanitos afuera del applet arriba
        for(Base basMalo : lklJuanitos){
            basMalo.setX((int) (Math.random() *(getWidth() - 
                    basMalo.getAncho())));
            basMalo.setY((int) (-1 * Math.random() *(getHeight() - 
                    basMalo.getAlto())));
        }
        
        
        //se obtiene el sonido 1 y 2 de nena
        URL urlSonidoNena1 = this.getClass().getResource("monkey1.wav");
        aucSonidoNena1 = getAudioClip (urlSonidoNena1);
        
        URL urlSonidoNena2 = this.getClass().getResource("monkey2.wav");
        aucSonidoNena2 = getAudioClip (urlSonidoNena2);
        
        //Imagen del Game Over
        URL urlImagenGO = this.getClass().getResource("GameOver.jpg");
        imaGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGO);
        
        //KeyListener
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0 && !bEsc) {
            if(!bP){
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        //actualizas a la nena dependiendo de su direccion
        switch(iDireccion) {
            case 1: {
                basNena.setY(basNena.getY()-1); 
                break;    //se mueve hacia arriba
            }
            case 2: {
                basNena.setY(basNena.getY()+1);
                break;    //se mueve hacia abajo
            }
            case 3: {
                basNena.setX(basNena.getX()-1);
                break;    //se mueve hacia izquierda
            }
            case 4: {
                basNena.setX(basNena.getX()+1); 
                break;    //se mueve hacia derecha	
            }
        }
        //avanzas a los fantasmas
        for(Base basMalo : lklFantasmas){
            basMalo.setX(basMalo.getX() + (int)(Math.random() * 2) + 3);
        }
        
        //para cada Fantasma, compruebo si llegan a la pared y llos regresas
        for(Base basMalo : lklFantasmas){
            if(basMalo.getX() + basMalo.getAncho() > getWidth()){
                basMalo.setX((int) (-1 * Math.random() *(getWidth() - 
                    basMalo.getAncho())));
                basMalo.setY((int) (Math.random() *(getHeight() - 
                    basMalo.getAlto())));
            }
        }
        for(Base basMalo : lklJuanitos){
            basMalo.setY(basMalo.getY()+iVelocidad);
        }
        
        //para cada Juanito, compruebo si llegan al suelo
        for(Base basMalo : lklJuanitos){
            if(basMalo.getY() + basMalo.getAlto() > getHeight()){
                basMalo.setX((int) (Math.random() *(getWidth() - 
                    basMalo.getAncho())));
                basMalo.setY((int) (-1 * Math.random() *(getHeight() - 
                    basMalo.getAlto())));
            }
        }
        
        //si se llega a 5 caidas, se quita una vida y aumenta la velocidad
        if(iCrashes >= 5){
            iVidas--;
            iVelocidad++;
            iCrashes = 0;
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        //colicion de Nena en el app la detiene
        switch(iDireccion){
            case 1: { // si se mueve hacia arriba 
                if(basNena.getY() < 0) { // y esta pasando el limite
                    iDireccion = 0;     // se cambia la direccion (detener)
                    basNena.setY(0);    //se mantiene en la orilla
                }
                break;    	
            }     
            case 2: { // si se mueve hacia abajo
                // y se esta saliendo del applet
                if(basNena.getY() + basNena.getAlto() > getHeight()) {
                    iDireccion = 0;     // se cambia la direccion
                    //se mantiene en la orilla
                    basNena.setY(getHeight()-basNena.getAlto());
                }
                break;    	
            } 
            case 3: { // si se mueve hacia izquierda 
                if(basNena.getX() < 0) { // y se sale del applet
                    iDireccion = 0;       // se cambia la direccion 
                    basNena.setX(0);       //se mantiene en la orilla
                }
                break;    	
            }    
            case 4: { // si se mueve hacia derecha 
                // si se esta saliendo del applet
                if(basNena.getX() + basNena.getAncho() > getWidth()) { 
                    iDireccion = 0;       // se cambia direccion
                    //se mantiene en la orilla
                    basNena.setX(getWidth()-basNena.getAncho());
                }
                break;    	
            }			
        }
        //Colicion de los Fantasmas
        for(Base basMalo : lklFantasmas){
            if (basNena.intersecta(basMalo)) {
                //Reproduzco el choque con fantasma
                aucSonidoNena2.play();
                //Regreso al fantasma en otra poscicion fuera de pantalla
                basMalo.setX((int) (-1 * Math.random() *(getWidth() - 
                    basMalo.getAncho())));
                basMalo.setY((int) (Math.random() *(getHeight() - 
                    basMalo.getAlto())));

                //mas 1 puntos
                iPuntos += 1;
              
            }  
        }
        //Colicion con los Juanitos
        for(Base basMalo : lklJuanitos){
            if (basNena.intersecta(basMalo)) {
                //Reproduzco el choque con Juanitos
                aucSonidoNena1.play();
                //Regreso al juanito en otra poscicion fuera de pantalla
                basMalo.setX((int) (Math.random() *(getWidth() - 
                    basMalo.getAncho())));
                basMalo.setY((int) (-1 * Math.random() *(getHeight() - 
                    basMalo.getAlto())));

                //mas 1 colicion de juanito
                iCrashes++;
              
            }  
        }
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        if(!bEsc && iVidas>0){
        // si la imagen ya se cargo
            if (basNena != null) {
                    //Dibuja la imagen de principal en el Applet
                    basNena.paint(graDibujo, this);
                    //Dibuja la imagen de los malos (fantasmas y Juanitos) en el Applet
                    for(Base basMalo : lklFantasmas){
                        basMalo.paint(graDibujo, this); 
                    }
                    for(Base basMalo : lklJuanitos){
                        basMalo.paint(graDibujo, this); 
                    }
                    //se cambia el "Font" a Times New Roman y con tamaño 30
                    graDibujo.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                    graDibujo.setColor(Color.RED);
                    //Se despliegan tus vidas restantes
                    graDibujo.drawString("Vidas: "+iVidas, 10, 30);
                    //Se despliega tu puntiacion
                    graDibujo.drawString("Puntuacion: "+iPuntos, 10, 50);


            } // sino se ha cargado se dibuja un mensaje 
            else {
                    //Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        }else{
            //Dibujamos el Game Over Justo en el Centro
            graDibujo.drawImage(imaGameOver,(getWidth()/2)-
                    imaGameOver.getWidth(this)/2,(getHeight()/2)-
                            imaGameOver.getHeight(this)/2, this);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono flecha para arriba
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {    
                iDireccion = 1;  // cambio la dirección arriba
        }
        // si presiono flecha para abajo
        else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {    
                iDireccion = 2;   // cambio la direccion para abajo
        }
        // si presiono flecha a la izquierda
        else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {    
                iDireccion = 3;   // cambio la direccion a la izquierda
        }
        // si presiono flecha a la derecha
        else if(keyEvent.getKeyCode() == KeyEvent.VK_D){    
                iDireccion = 4;   // cambio la direccion a la derecha
        } 
        else if(keyEvent.getKeyCode() == KeyEvent.VK_P){    
                bP=!bP;   // cambio la variable de pausa
        }
        else if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){    
                bEsc=!bEsc;   // cambio la variable para temrinar el juego
        }
    }
}