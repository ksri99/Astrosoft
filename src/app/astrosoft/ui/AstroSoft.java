/**
 * AstroSoft.java
 *
 * Created on December 13, 2002, 5:16 PM
 *
 * @author  E. Rajasekar
 */
package app.astrosoft.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import app.astrosoft.beans.BirthData;
import app.astrosoft.beans.PlanetChartData;
import app.astrosoft.consts.Ayanamsa;
import app.astrosoft.consts.Command;
import app.astrosoft.consts.DisplayStrings;
import app.astrosoft.consts.Language;
import app.astrosoft.consts.Nakshathra;
import app.astrosoft.consts.Rasi;
import app.astrosoft.consts.Varga;
import app.astrosoft.core.Compactibility;
import app.astrosoft.core.Ephemeris;
import app.astrosoft.core.Horoscope;
import app.astrosoft.core.Muhurtha;
import app.astrosoft.core.Panchang;
import app.astrosoft.core.Ephemeris.Mode;
import app.astrosoft.export.AstrosoftExporter;
import app.astrosoft.pref.AstrosoftPref;
import app.astrosoft.pref.AstrosoftPref.Preference;
import app.astrosoft.ui.comp.AstrosoftMenuBar;
import app.astrosoft.ui.comp.AstrosoftToolBar;
import app.astrosoft.ui.comp.Chart;
import app.astrosoft.ui.comp.DateListener;
import app.astrosoft.ui.dlg.BirthInputDialog;
import app.astrosoft.ui.dlg.CompactibilityInputDialog;
import app.astrosoft.ui.dlg.ExportDialog;
import app.astrosoft.ui.dlg.MuhurthaInput;
import app.astrosoft.ui.dlg.ComputeNumberDialog;
import app.astrosoft.ui.dlg.OptionDialog;
import app.astrosoft.ui.dlg.PlaceOptionDialog;
import app.astrosoft.ui.dlg.PrintDialog;
import app.astrosoft.ui.dlg.TimeInputDialog;
import app.astrosoft.ui.util.UIConsts;
import app.astrosoft.ui.view.AshtavargaView;
import app.astrosoft.ui.view.BhavaView;
import app.astrosoft.ui.view.CompactibilityView;
import app.astrosoft.ui.view.EphemerisView;
import app.astrosoft.ui.view.FindNameView;
import app.astrosoft.ui.view.InfoView;
import app.astrosoft.ui.view.MuhurthaPanel;
import app.astrosoft.ui.view.PanchangView;
import app.astrosoft.ui.view.PlanetView;
import app.astrosoft.ui.view.ShadBalaView;
import app.astrosoft.ui.view.VargaChartView;
import app.astrosoft.ui.view.ViewContainer;
import app.astrosoft.ui.view.ViewManager;
import app.astrosoft.ui.view.VimDasaView;
import app.astrosoft.ui.view.YogaCombinationsView;
import app.astrosoft.ui.view.ViewManager.View;
import app.astrosoft.util.AstroUtil;
import app.astrosoft.util.AstrosoftFileFilter;
import app.astrosoft.util.FileOps;

public class AstroSoft extends javax.swing.JFrame implements
		AstrosoftActionHandler {

	private Horoscope h;

	private Ephemeris eph;	

	private Panchang pan;

	private AstrosoftMenuBar mbar;

	private AstrosoftToolBar toolbar;

	private JPanel home;

	private AstrosoftActionManager actionMgr;

	private ViewManager viewManager;

	private Compactibility compactibility;

	private Muhurtha muhurtha;

	private static AstrosoftPref preferences = new AstrosoftPref();

	//public static Calendar today = new GregorianCalendar(); //AstroSoft.getPreferences().getPlace().astrosoftTimeZone().getTimeZone());

	public static Calendar today = new GregorianCalendar(AstroSoft.getPreferences().getPlace().astrosoftTimeZone().getTimeZone());

	//Until Abslayout is completely removed:

	private static Rectangle bounds = new Rectangle(0,0, getScreenSize().width, getScreenSize().height);

	private AstroSoft() {

		initComponents();

		System.out.println(getScreenSize());
		setPreferredSize(getScreenSize());
		//this.setPreferredSize(new Dimension(800, 600));

		// show();
		getContentPane().setLayout(new BorderLayout());
		formMenuAndToolBar();
		home = new JPanel();
		home.setLayout(null);
		//home.setLayout(new BorderLayout());

		// home.setBounds(0,0,screenSize.width , screenSize.height);
		home.setBounds(bounds);

		enableActions(false);
		setVisible(true);
		pack();
		viewManager = new ViewManager(new AstrosoftViewContainer());

	}

	private AstroSoft(String[] args){

		this();
		System.out.println("Logging properties: " + System.getProperty("java.util.logging.config.file"));
		
		if(args.length > 0 && args[0] != null){
			openHoroscope(args[0]);
		}
	}

	public void formMenuAndToolBar() {

		actionMgr = new AstrosoftActionManager(this);

		mbar = new AstrosoftMenuBar(actionMgr);
		setJMenuBar(mbar);

		toolbar = new AstrosoftToolBar(actionMgr);

		this.add(toolbar, BorderLayout.PAGE_START);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() { // GEN-BEGIN:initComponents
		setTitle("AstroSoft");
		setName("AstroSoft");
		addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(java.awt.event.WindowEvent evt) {

				exitForm(evt);

			}

		});

	} // GEN-END:initComponents

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) { // GEN-FIRST:event_exitForm
		System.exit(0);

	} // GEN-LAST:event_exitForm

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		try {

			//FIXME:
			 //UIManager.setLookAndFeel(
			 //"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			// UIManager.setLookAndFeel(new
			// com.incors.plaf.kunststoff.KunststoffLookAndFeel());
			// UIManager.setLookAndFeel(new
			// net.sourceforge.mlf.metouia.MetouiaLookAndFeel());
			//UIManager
				//.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");

			// Skin theSkinToUse = SkinLookAndFeel.loadThemePack(args[0] +
			// "/lib/" + "themepack.zip");
			// SkinLookAndFeel.setSkin(theSkinToUse);

			// finally set the Skin Look And Feel
			// UIManager.setLookAndFeel(new SkinLookAndFeel());

			 //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticLookAndFeel");
			 //com.jgoodies.looks.plastic.PlasticLookAndFeel.setMyCurrentTheme(new
			 //ExperienceGreen());

			//Plastic3DLookAndFeel.setMyCurrentTheme(new ExperienceGreen());
   		    //UIManager.setLookAndFeel(new Plastic3DLookAndFeel());

			/*
			 * Skin theSkinToUse =
			 * SkinLookAndFeel.loadThemePack("C:\\Data\\AstroSoft\\lib\\toxicthemepack.zip");
			 * SkinLookAndFeel.setSkin(theSkinToUse);
			 *
			 * UIManager.setLookAndFeel(new SkinLookAndFeel());
			 */


		} catch (Exception e) {

			e.printStackTrace();
		}

		setUIDefaults();
		AstroSoft as = new AstroSoft(args);
		//as.setVisible(true);
	}

	private static void setUIDefaults() {

		try {
			UIManager.setLookAndFeel(UIConsts.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//JFrame.setDefaultLookAndFeelDecorated(true);
		Map defaults = UIConsts.getUIDefaults();

		for(Object key : defaults.keySet()){
			UIManager.put(key, defaults.get(key));
		}
	}

	public void computeHoroscope(BirthData birthData){
		h = new Horoscope(birthData);
		viewManager.showView(View.CHART_VIEW);
	}

	public JPanel createChartView() {


		JPanel infoView = new InfoView(h.getHoroscopeInfo(), new Point(20,20));

		//FIXME:
		/*EnumMap<Planet, Integer> pos = new EnumMap<Planet, Integer>(Planet.class);

		pos.put(Planet.Sun, 1);
    	pos.put(Planet.Moon, 1);
    	pos.put(Planet.Mars, 1);
    	pos.put(Planet.Mercury, 1);
    	pos.put(Planet.Jupiter, 1);
    	pos.put(Planet.Venus, 1);
    	pos.put(Planet.Saturn, 1);
    	pos.put(Planet.Rahu, 1);
    	pos.put(Planet.Ketu, 1);
    	pos.put(Planet.Ascendant, 1);*/

		//Chart rasi = new Chart(new PlanetChartData(Varga.Rasi.toString(), h.getDivChart(Varga.Rasi), h.getPlanetDirection(), DisplayFormat.SYMBOL), new Dimension(360,320));

    	Chart rasi = new Chart(new PlanetChartData(Varga.Rasi, h.getPlanetaryInfo()), new Dimension(360,320));

		Chart navamsa = new Chart(new PlanetChartData(Varga.Navamsa, h.getPlanetaryInfo()), new Dimension(360,320));

		JPanel view = new JPanel();
		view.add(infoView);
		view.add(rasi);
		view.add(navamsa);
		view.setBounds(bounds);

		return view;

	}

	public void printHoroscope() {
		new PrintDialog(this);
	}

	public JPanel createShadbalaView(){
		if(h.getShadBala() != null){
			return new ShadBalaView(h.getTitle(), h.getShadBala(), new Point(30,20));
		}else{
			OptionDialog.showDialog("No ShadBala for Birth Year < 1900", JOptionPane.ERROR_MESSAGE);
			return createChartView();
		}
	}

	public void showPanchang(Calendar date) {

		pan = new Panchang(date);
		viewManager.showView(View.PANCHANG_VIEW);

	}

	public void displayCompactibility(Horoscope boy, Horoscope girl) {

		compactibility = new Compactibility(boy, girl);
		actionMgr.enableAction(Command.PRINT, true);
		viewManager.showView(View.COMPACTIBILITY_VIEW);
	}

	public void displayCompactibility(String bName, String gName, Rasi bRasi,
			Rasi gRasi, Nakshathra bNak, Nakshathra gNak) {

		compactibility = new Compactibility(bName, gName, bNak,
				gNak, bRasi, gRasi);
		actionMgr.enableAction(Command.PRINT, true);
		viewManager.showView(View.COMPACTIBILITY_VIEW);
	}

	public void displayMuhurtha(Muhurtha m) {

		muhurtha = m;
		viewManager.showView(View.MUHURTHA_VIEW);
		// Muhurtha m = new Muhurtha(today, Rasi.Mesha, Nakshathra.Mrigasira,
		// false, false, 1);
	}


	public void newHoroscope() {

		new BirthInputDialog(this);
		//FIXME: Should not enable menus if cancel clicked
		enableActions(true);
	}

	public void openHoroscope() {

		String selectedFile = FileOps.openFileDialog(this, FileOps.FileDialogMode.OPEN, AstrosoftFileFilter.HOROSCOPE_EXTN);
		
		if (selectedFile != null) {
			openHoroscope(selectedFile);
		}
	}

	public void openHoroscope(String horoscopeFile) {

		h = Horoscope.createFromFile(horoscopeFile);

		if (h != null) {
			enableActions(true);

		} else {
			OptionDialog.showDialog(" Invalid File ", JOptionPane.ERROR_MESSAGE);
			this.repaint();
			this.setVisible(true);
			return;
		}
		viewManager.showView(View.CHART_VIEW);
	}

	public void saveHoroscope() {

		if (h != null){
			String selectedFile = FileOps.openFileDialog(this, FileOps.FileDialogMode.SAVE, AstrosoftFileFilter.HOROSCOPE_EXTN);
			if (selectedFile != null) {
				h.saveToFile(selectedFile);
			}
		}
	}

	public void openCompactibility(){
		String fileName = FileOps.openFileDialog(this, FileOps.FileDialogMode.OPEN, AstrosoftFileFilter.COMPACTIBILITY_EXTN);
		
		if (fileName != null) {
			compactibility = Compactibility.createFromXML(fileName);
		
			if (compactibility != null){
				actionMgr.enableAction(Command.PRINT, true);
				viewManager.showView(View.COMPACTIBILITY_VIEW);
			}
		}
	}

	public void editCompactibility(){

		if (compactibility != null){
			if (compactibility.hasHoroscope()){
				new CompactibilityInputDialog(this, compactibility.getBoyBirthData(), compactibility.getGirlBirthData());
			}else{
				new CompactibilityInputDialog(this,
						compactibility.getBoyName(),
						compactibility.getGirlName(),
						compactibility.getBoyRasi(),
						compactibility.getGirlRasi(),
						compactibility.getBoyNak(),
						compactibility.getGirlNak());
			}
		}
	}

	public void saveCompactibility(){

		if (compactibility != null){
			String selectedFile = FileOps.openFileDialog(this, FileOps.FileDialogMode.SAVE, AstrosoftFileFilter.COMPACTIBILITY_EXTN);
			
			if (selectedFile != null) {
				compactibility.saveToXML(selectedFile);
			}
		}
	}
	public void editHoroscope() {

		new BirthInputDialog(this, h.getBirthData());

	}

	public void exportHoroscope2Pdf(){

		if (h != null){

			String selectedFile = FileOps.openFileDialog(this, FileOps.FileDialogMode.SAVE, AstrosoftFileFilter.PDF_EXTN);
			FutureTask<Object> task = AstrosoftExporter.export2Pdf(AstrosoftExporter.Type.Horosocope, h, selectedFile);
			
			ExportDialog exportDlg = new ExportDialog(this,"Export Horosocope ", task,selectedFile);
		}
	}

	public void exportCompactibility2Pdf(){

		if (compactibility != null){

			String selectedFile = FileOps.openFileDialog(this, FileOps.FileDialogMode.SAVE, AstrosoftFileFilter.PDF_EXTN);
			AstrosoftExporter.export2Pdf(AstrosoftExporter.Type.Compactibility, compactibility, selectedFile);
		}
	}

	public void setWaitCursor(){
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public void setDefaultCursor(){
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void computeNumeroNumber(){
		new ComputeNumberDialog(this);
	}
	
	private void optionAyanamsaChanged(Ayanamsa ayanamsa) {

		if (eph != null) {

			eph.setAyanamsa(ayanamsa);
			viewManager.showView(View.EPHEMERIS_VIEW);
		}

		if (pan != null) {

			viewManager.showView(View.PANCHANG_VIEW);
		}

		if (h != null) {
			h.setAyanamsa(ayanamsa);
			viewManager.showView(View.CHART_VIEW);
		}

	}

	private void optionLangChanged(String lang) {

		if (eph != null) {

			viewManager.showView(View.EPHEMERIS_VIEW);

		}

		if (pan != null) {

			viewManager.showView(View.PANCHANG_VIEW);

		}

		if (h != null) {

			h.languageChanged();
			viewManager.showView(View.CHART_VIEW);

		}

	}

	public void optionChanged(final Preference preference){

		DateListener listener = new DateListener(){
			public void dateChanged(Date date) {
				preferences.preferenceChanged(preference, date);
			}
		};

		switch(preference){
			case Place: new PlaceOptionDialog(this);
						break;
			case EphCalcTime:
							  new TimeInputDialog(DisplayStrings.EPH_TIME_STR, this, AstroUtil.doubleTimeToDate(preferences.getEphCalcTime()), listener);
							  break;
			case PanCalcTime: new TimeInputDialog(DisplayStrings.PAN_TIME_STR, this, AstroUtil.doubleTimeToDate(preferences.getPanCalcTime()), listener);
							  break;

		}
	}
	public void enableActions(boolean enable) {

		EnumSet<Command> actions = EnumSet.of(Command.SAVE,Command.SAVE_COMPACTIBILITY,
				Command.EDIT_COMPACTIBILITY,
				Command.EDIT_CHART,
				Command.CHART_VIEW, Command.PLANET_POS_VIEW,
				Command.BHAVA_POS_VIEW, Command.DIV_CHART_VIEW,
				Command.ASHTAVARGA_VIEW, Command.SHADBALA_VIEW,
				Command.YOGA_COMBINATIONS_VIEW, Command.DASAS_VIEW);

		actionMgr.enableActions(actions, enable);
	}


	public void viewChanged(View view) {

		switch(view){

			case EPHEMERIS_VIEW: eph = new Ephemeris(AstroUtil.getCalendar(), Mode.Daily);
								 viewManager.showView(view);
								 break;

			case PANCHANG_VIEW: pan = new Panchang(today);
			                    viewManager.showView(view);
								break;

			case COMPACTIBILITY_VIEW: new CompactibilityInputDialog(this);
								break;
			case MUHURTHA_VIEW: new MuhurthaInput(this);
								break;
								
			
			default: viewManager.showView(view);
		}
	}

	public void refreshUI() {

		getContentPane( ).removeAll( );

		//getContentPane().remove(home);
		getContentPane().add(toolbar, BorderLayout.PAGE_START);

		getContentPane().add(home, BorderLayout.CENTER);
		this.repaint();

		this.setVisible(true);

	}

	public void optionChanged(Preference preference, Object value) {

		preferences.preferenceChanged(preference, value);

		switch (preference) {
		case Ayanamsa:
			optionAyanamsaChanged((Ayanamsa) value);
			break;
		case Language:
			optionLangChanged(((Language) value).name());
			break;
		}


	}

	public static AstrosoftPref getPreferences(){
		return preferences;
	}

   /**
	 * @return Returns the horoscope.
	 */
	public Horoscope getHoroscope() {
		return h;
	}
	
	public Compactibility getCompactibility() {
		return compactibility;
	}

	public static Dimension getScreenSize(){

		return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		//return new Dimension(800,600);
	}


	private class AstrosoftViewContainer implements ViewContainer{
		public void addView(JPanel view){
			home.removeAll();
			//home.add(view, BorderLayout.CENTER);
			home.add(view);
			refreshUI();
		}

		public JPanel createView(View view) {

			switch(view){
				case CHART_VIEW:
					return createChartView();
				case DASAS_VIEW:
					return new VimDasaView(h.getTitle(), h.getVimshottariDasa());
				case BHAVA_POS_VIEW:
					return new BhavaView(h.getTitle(), h.getHousePosition());
				case PLANET_POS_VIEW:
					return new PlanetView(h.getTitle(), h.getPlanetaryInfo());
				case SHADBALA_VIEW:
					return createShadbalaView();
				case DIV_CHART_VIEW:
					return new VargaChartView(h.getTitle(), h.getPlanetaryInfo());
				case ASHTAVARGA_VIEW:
					return new AshtavargaView(h.getTitle(), h.getAshtaVarga(), new Point(30,20));
				case PANCHANG_VIEW:
					return new PanchangView(pan, new Point(30,20));
				case EPHEMERIS_VIEW:
					return new EphemerisView(eph, new Point(10,2));

				case COMPACTIBILITY_VIEW:
					actionMgr.enableActions(EnumSet.of(Command.SAVE_COMPACTIBILITY), true);
					return new CompactibilityView(DisplayStrings.MRAG_COMP_STR.toString(Language.ENGLISH), compactibility, new Point(30,20));

				case MUHURTHA_VIEW:
					return new MuhurthaPanel(muhurtha, AstroSoft.this);

				case FIND_NAME_VIEW:
					return new FindNameView();
					
				case YOGA_COMBINATIONS_VIEW: 
					return new YogaCombinationsView(h.getTitle(),h.getYogaCombinations(),h.getPlanetaryInfo());
				 
			}
			return null;
		}
	}


}
