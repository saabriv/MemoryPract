package com.example.memorypract.data.modelos;

public class MetricasData {
    private String semanaTexto;
    private String rangoSemana;

    private int seriesRealizadas;
    private int seriesObjetivo;
    private int seriesAnterior;
    private String seriesTag;

    private int coloresAcertados;
    private int coloresObjetivo;
    private int coloresAnterior;
    private String coloresTag;

    private int animalesReconocidos;
    private int animalesObjetivo;
    private int animalesAnterior;
    private String animalesTag;

    private int objetosIdentificados;
    private int objetosObjetivo;
    private int objetosAnterior;
    private String objetosTag;

    private String resumenMesNombre;
    private int seriesTotalesMes;
    private int aciertosTotalesMes;
    private int porcentajeCumplimiento;

    public MetricasData(String semanaTexto, String rangoSemana,
                        int seriesRealizadas, int seriesObjetivo, int seriesAnterior, String seriesTag,
                        int coloresAcertados, int coloresObjetivo, int coloresAnterior, String coloresTag,
                        int animalesReconocidos, int animalesObjetivo, int animalesAnterior, String animalesTag,
                        int objetosIdentificados, int objetosObjetivo, int objetosAnterior, String objetosTag,
                        String resumenMesNombre, int seriesTotalesMes, int aciertosTotalesMes, int porcentajeCumplimiento) {
        this.semanaTexto = semanaTexto;
        this.rangoSemana = rangoSemana;
        this.seriesRealizadas = seriesRealizadas;
        this.seriesObjetivo = seriesObjetivo;
        this.seriesAnterior = seriesAnterior;
        this.seriesTag = seriesTag;
        this.coloresAcertados = coloresAcertados;
        this.coloresObjetivo = coloresObjetivo;
        this.coloresAnterior = coloresAnterior;
        this.coloresTag = coloresTag;
        this.animalesReconocidos = animalesReconocidos;
        this.animalesObjetivo = animalesObjetivo;
        this.animalesAnterior = animalesAnterior;
        this.animalesTag = animalesTag;
        this.objetosIdentificados = objetosIdentificados;
        this.objetosObjetivo = objetosObjetivo;
        this.objetosAnterior = objetosAnterior;
        this.objetosTag = objetosTag;
        this.resumenMesNombre = resumenMesNombre;
        this.seriesTotalesMes = seriesTotalesMes;
        this.aciertosTotalesMes = aciertosTotalesMes;
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public String getSemanaTexto() { return semanaTexto; }
    public String getRangoSemana() { return rangoSemana; }

    public int getSeriesRealizadas() { return seriesRealizadas; }
    public int getSeriesObjetivo() { return seriesObjetivo; }
    public int getSeriesAnterior() { return seriesAnterior; }
    public String getSeriesTag() { return seriesTag; }

    public int getColoresAcertados() { return coloresAcertados; }
    public int getColoresObjetivo() { return coloresObjetivo; }
    public int getColoresAnterior() { return coloresAnterior; }
    public String getColoresTag() { return coloresTag; }

    public int getAnimalesReconocidos() { return animalesReconocidos; }
    public int getAnimalesObjetivo() { return animalesObjetivo; }
    public int getAnimalesAnterior() { return animalesAnterior; }
    public String getAnimalesTag() { return animalesTag; }

    public int getObjetosIdentificados() { return objetosIdentificados; }
    public int getObjetosObjetivo() { return objetosObjetivo; }
    public int getObjetosAnterior() { return objetosAnterior; }
    public String getObjetosTag() { return objetosTag; }

    public String getResumenMesNombre() { return resumenMesNombre; }
    public int getSeriesTotalesMes() { return seriesTotalesMes; }
    public int getAciertosTotalesMes() { return aciertosTotalesMes; }
    public int getPorcentajeCumplimiento() { return porcentajeCumplimiento; }
}
