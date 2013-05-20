package com.example.memoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class MainActivity extends Activity {

	
	private actualizarCartas handler;
    private static int fila_total= -1;
	private static int colum_total= -1;
	private Context context;
	private int [] [] cartas;
	private List<Drawable> images;
	private Drawable Cartareversa;
	private ButtonListener buttonListener;
	private TableLayout tablafull;
	private Carta primeraCarta;
	private Carta segundaCarta;
	private ArrayAdapter<String> listAdapter ;  
	int turnos;
	private ListView s;
	private static Object lock = new Object();
	@Override
	
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cargarImagen();
		handler = new actualizarCartas();
		setContentView(R.layout.activity_main);
		
		Cartareversa =  getResources().getDrawable(R.drawable.icono);
		buttonListener = new ButtonListener();
		tablafull = (TableLayout)findViewById(R.id.TableLayout03);
		context  = tablafull.getContext();
		
		s = (ListView)findViewById(R.id.Spinner01);
	     listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow); 
	     listAdapter.add( "Nuevo Juego" );  
	     listAdapter.add( "Salir" ); 
	     s.setAdapter(listAdapter);
	     
	     s.setOnItemClickListener(new OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			      
			    	int x,y;
		  			Log.i("onCreate()",""+position);
		  			switch (position) {
					case 0:
						juego();
						break;
					case 1:
						return;
					default:
						return;
					}
			    	
			    }
			});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
    private void cargarImagen() {
    	images = new ArrayList<Drawable>();
    	
    	images.add(getResources().getDrawable(R.drawable.car1));
    	images.add(getResources().getDrawable(R.drawable.car2));
    	images.add(getResources().getDrawable(R.drawable.car3));
    	images.add(getResources().getDrawable(R.drawable.car4));
    	images.add(getResources().getDrawable(R.drawable.car5));
    	images.add(getResources().getDrawable(R.drawable.car6));
    	images.add(getResources().getDrawable(R.drawable.car7));
    	images.add(getResources().getDrawable(R.drawable.car8));
    	images.add(getResources().getDrawable(R.drawable.car9));
    	images.add(getResources().getDrawable(R.drawable.car10));
    	images.add(getResources().getDrawable(R.drawable.car11));
    	images.add(getResources().getDrawable(R.drawable.car12));
    	images.add(getResources().getDrawable(R.drawable.car13));
    	images.add(getResources().getDrawable(R.drawable.car14));
    	images.add(getResources().getDrawable(R.drawable.car15));
    	images.add(getResources().getDrawable(R.drawable.car16));
    	images.add(getResources().getDrawable(R.drawable.car17));
    	images.add(getResources().getDrawable(R.drawable.car18));
    	images.add(getResources().getDrawable(R.drawable.car19));
    	images.add(getResources().getDrawable(R.drawable.car20));
    	images.add(getResources().getDrawable(R.drawable.car21));
		
	}
    
    private void juego() {
    	fila_total = 4;
    	colum_total= 4;
    	
    	cartas = new int [fila_total] [colum_total];
    	
    	
    	tablafull.removeView(findViewById(R.id.TableRow01));
    	tablafull.removeView(findViewById(R.id.TableRow04));
    	
    	TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
    	tr.removeAllViews();
    	
    	tablafull = new TableLayout(context);
    	tr.addView(tablafull);
    	
    	 for (int y = 0; y < fila_total; y++) {
    		 tablafull.addView(crearFila(y));
          }
    	 
    	 primeraCarta=null;
    	 cargarCartas();
    	 
    	 turnos=0;
    	 ((TextView)findViewById(R.id.tv1)).setText("Intentos: "+turnos);
    	 
			
	}
    
    private TableRow crearFila(int y){
   	 TableRow fila = new TableRow(context);
   	fila.setHorizontalGravity(Gravity.CENTER);
        
        for (int x = 0; x < colum_total; x++) {
        	fila.addView(crearBotones(x,y));
        }
        return fila;
   }
    
    private View crearBotones(int x, int y){
    	Button button = new Button(context);
    	button.setBackgroundDrawable(Cartareversa);
    	button.setId(100*x+y);
    	button.setOnClickListener(buttonListener);
    	return button;
    }
    
    private void cargarCartas(){
		try{
	    	int size = fila_total*colum_total;
	    	
	    	Log.i("loadCards()","size=" + size);
	    	
	    	ArrayList<Integer> list = new ArrayList<Integer>();
	    	
	    	for(int i=0;i<size;i++){
	    		list.add(new Integer(i));
	    	}
	    	
	    	
	    	Random r = new Random();
	    
	    	for(int i=size-1;i>=0;i--){
	    		int t=0;
	    		
	    		if(i>0){
	    			t = r.nextInt(i);
	    		}
	    		
	    		t=list.remove(t).intValue();
	    		cartas[i%colum_total][i/colum_total]=t%(size/2);
	    		
	    		Log.i("loadCards()", "card["+(i%colum_total)+
	    				"]["+(i/colum_total)+"]=" + cartas[i%colum_total][i/colum_total]);
	    	}
	    }
		catch (Exception e) {
			Log.e("cargarcartas()", e+"");
		}
		
    }
    
    
    class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.i("onClick()","qqqq");
			synchronized (lock) {
				if(primeraCarta!=null && segundaCarta != null){
					Log.i("onClick()","nuuuu");
					return;
				}
				Log.i("onClick()","seee");
				int id = v.getId();
				int x = id/100;
				int y = id%100;
				turnoCarta((Button)v,x,y);
				
			}
			
		}

		private void turnoCarta(Button button,int x, int y) {
			button.setBackgroundDrawable(images.get(cartas[x][y]));
			
			if(primeraCarta==null){
				primeraCarta = new Carta(button,x,y);
			}
			else{ 
				
				if(primeraCarta.x == x && primeraCarta.y == y){
					return;
				}
					
				segundaCarta = new Carta(button,x,y);
				
				turnos++;
				((TextView)findViewById(R.id.tv1)).setText("Intentos: "+turnos);
				
			
				TimerTask tt = new TimerTask() {
					
					@Override
					public void run() {
						try{
							synchronized (lock) {
							  handler.sendEmptyMessage(0);
							}
						}
						catch (Exception e) {
							Log.e("error en sincronzacion", e.getMessage());
						}
					}
				};
				
				  Timer t = new Timer(false);
			        t.schedule(tt, 1300);
			}
			
				
		   }
			
		}
    
    
class actualizarCartas extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		synchronized (lock) {
    			chequear();
    		}
    	}
    	 public void chequear(){
    	    	if(cartas[segundaCarta.x][segundaCarta.y] == cartas[primeraCarta.x][primeraCarta.y]){
    				primeraCarta.button.setVisibility(View.INVISIBLE);
    				segundaCarta.button.setVisibility(View.INVISIBLE);
    			}
    			else {
    				segundaCarta.button.setBackgroundDrawable(Cartareversa );
    				primeraCarta.button.setBackgroundDrawable(Cartareversa );
    			}
    	    	
    	    	primeraCarta=null;
    	    	segundaCarta=null;
    	    }
    }

}
