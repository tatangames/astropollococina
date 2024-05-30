package com.tatanstudios.astropollococina.fragmentos.historial;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.principal.PrincipalActivity;

import java.util.Calendar;

public class FragmentHistorial extends Fragment {


    boolean seguro1 = false;
    boolean seguro2 = false;

    String fecha1 = "";
    String fecha2 = "";

    String fecha1Ba = "";
    String fecha2Ba = "";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private static final String CERO = "0";
    private static final String GUION = "-";
    private static final String BARRA = "/";


    Button btnFecha1;
    Button btnFecha2;
    Button btnBuscar;
    TextView txtFecha1;
    TextView txtFecha2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_historial, container, false);

        btnFecha1 = vista.findViewById(R.id.btnFecha1);
        btnFecha2 = vista.findViewById(R.id.btnFecha2);
        btnBuscar = vista.findViewById(R.id.btnBuscar);
        txtFecha1 = vista.findViewById(R.id.txtFecha1);
        txtFecha2 = vista.findViewById(R.id.txtFecha2);


        ((PrincipalActivity) getActivity()).setActionBarTitle(getString(R.string.historial));

        btnBuscar.setOnClickListener( v-> buscar());

        btnFecha1.setOnClickListener(v -> elegirfecha1());
        btnFecha2.setOnClickListener(v -> elegirfecha2());

        return vista;
    }

    void elegirfecha1(){
        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado
            txtFecha1.setText(diaFormateado + GUION + mesFormateado + GUION + year);

            fecha1 = year + GUION + mesFormateado + GUION + diaFormateado;
            fecha1Ba = year + BARRA + mesFormateado + BARRA + diaFormateado;
            seguro1 = true;
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    void elegirfecha2(){
        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado
            txtFecha2.setText(diaFormateado + GUION + mesFormateado + GUION + year);

            fecha2 = year + GUION + mesFormateado + GUION + diaFormateado;
            fecha2Ba = year + BARRA + mesFormateado + BARRA + diaFormateado;
            seguro2 = true;
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    void buscar(){
        if(seguro1 && seguro2){

            // BUSCAR

            FragmentListaHistorial fragmentInfo = new FragmentListaHistorial();

            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
            if (currentFragment.getClass().equals(fragmentInfo.getClass())) return;

            Bundle bundle = new Bundle();
            bundle.putString("FECHA1", String.valueOf(fecha1));
            bundle.putString("FECHA2", String.valueOf(fecha2));
            fragmentInfo.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContenedor, fragmentInfo)
                    .addToBackStack(null)
                    .commit();

        }else{
            faltaFecha();
        }
    }

    void faltaFecha(){

        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Fecha Necesaria");
        pDialog.setContentText("La Fecha 'DE' y 'HASTA' es Requerida");
        pDialog.setConfirmText("Completar");
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();

                });
        pDialog.show();

    }


}
