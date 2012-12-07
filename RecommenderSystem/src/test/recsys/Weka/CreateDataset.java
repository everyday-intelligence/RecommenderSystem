package test.recsys.Weka;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.DomainDAO.MovieLensItemDataParser;

import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CreateDataset {
	private static String itemsFile = "database/MovieLens/ml-100K/u.item";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() throws ParseException {
		 FastVector      atts;
	     FastVector      attsRel;
	     FastVector      attVals;
	     FastVector      attValsRel;
	     Instances       data;
	     Instances       dataRel;
	     double[]        vals;
	     double[]        valsRel;
	     int             i;
	 
	     // 1. set up attributes
	     atts = new FastVector();
	     // - numeric
	     atts.addElement(new Attribute("att1"));
	     // - nominal
	     attVals = new FastVector();
	     for (i = 0; i < 5; i++)
	       attVals.addElement("val" + (i+1));
	     atts.addElement(new Attribute("att2", attVals));
	     // - string
	     atts.addElement(new Attribute("att3", (FastVector) null));
	     // - date
	     atts.addElement(new Attribute("att4", "yyyy-MM-dd"));
	     // - relational
	     attsRel = new FastVector();
	     // -- numeric
	     attsRel.addElement(new Attribute("att5.1"));
	     // -- nominal
	     attValsRel = new FastVector();
	     for (i = 0; i < 5; i++)
	       attValsRel.addElement("val5." + (i+1));
	     attsRel.addElement(new Attribute("att5.2", attValsRel));
	     dataRel = new Instances("att5", attsRel, 0);
	     atts.addElement(new Attribute("att5", dataRel, 0));
	 
	     // 2. create Instances object
	     data = new Instances("MyRelation", atts, 0);
	 
	     // 3. fill with data
	     // first instance
	     vals = new double[data.numAttributes()];
	     // - numeric
	     vals[0] = Math.PI;
	     // - nominal
	     vals[1] = attVals.indexOf("val3");
	     // - string
	     vals[2] = data.attribute(2).addStringValue("This is a string!");
	     // - date
	     vals[3] = data.attribute(3).parseDate("2001-11-09");
	     // - relational
	     dataRel = new Instances(data.attribute(4).relation(), 0);
	     // -- first instance
	     valsRel = new double[2];
	     valsRel[0] = Math.PI + 1;
	     valsRel[1] = attValsRel.indexOf("val5.3");
	     dataRel.add(new Instance(1.0, valsRel));
	     // -- second instance
	     valsRel = new double[2];
	     valsRel[0] = Math.PI + 2;
	     valsRel[1] = attValsRel.indexOf("val5.2");
	     dataRel.add(new Instance(1.0, valsRel));
	     vals[4] = data.attribute(4).addRelation(dataRel);
	     // add
	     data.add(new Instance(1.0, vals));
	 
	     // second instance
	     vals = new double[data.numAttributes()];  // important: needs NEW array!
	     // - numeric
	     vals[0] = Math.E;
	     // - nominal
	     vals[1] = attVals.indexOf("val1");
	     // - string
	     vals[2] = data.attribute(2).addStringValue("And another one!");
	     // - date
	     vals[3] = data.attribute(3).parseDate("2000-12-01");
	     // - relational
	     dataRel = new Instances(data.attribute(4).relation(), 0);
	     // -- first instance
	     valsRel = new double[2];
	     valsRel[0] = Math.E + 1;
	     valsRel[1] = attValsRel.indexOf("val5.4");
	     dataRel.add(new Instance(1.0, valsRel));
	     // -- second instance
	     valsRel = new double[2];
	     valsRel[0] = Math.E + 2;
	     valsRel[1] = attValsRel.indexOf("val5.1");
	     dataRel.add(new Instance(1.0, valsRel));
	     vals[4] = data.attribute(4).addRelation(dataRel);
	     // add
	     data.add(new Instance(1.0, vals));
	 
	     // 4. output data
	     System.out.println(data);
	     System.out.println("one by one");
	     for(int j=0;j<data.numInstances();j++){
	    	 System.out.println(data.instance(j));
	     }
	}
	
	
	@Test
	public void testCreateDataset(){
		//List<Item> items = MovieLens100KDataReader.findItemsFile(this.itemsFile);
		FastVector attributes = new FastVector();
	     // - numeric
		attributes.addElement(new Attribute("age"));
	     // - nominal
	    FastVector sexes = new FastVector();
	    sexes.addElement("Homme");
	    sexes.addElement("Femme");
	    attributes.addElement(new Attribute("sexe", sexes));
	    Instances data = new Instances("MyData", attributes, 0);

	    //instance 1
	    double[] vals = new double[data.numAttributes()];
	    // - age
	    vals[0] = 25;
	    // - sexe (nominal)
	    vals[1] = sexes.indexOf("Homme");
	    data.add(new Instance(1,vals));
	    
	    //instance 2
	    // - age
	    vals[0] = 28;
	    // - sexe (nominal)
	    vals[1] = sexes.indexOf("Femme");
	    data.add(new Instance(1,vals));
	    
	    System.out.println(data);
	}
	
	@Test
	public void testCreateDataset2(){
		//List<Item> items = MovieLens100KDataReader.findItemsFile(this.itemsFile);
		FastVector attributes = new FastVector();
	     // - numeric
		attributes.addElement(new Attribute("age"));
	     // - nominal
	    FastVector sexes = new FastVector();
	    attributes.addElement(new Attribute("sexe", sexes));
	    Instances data = new Instances("MyData", attributes, 0);

	    //instance 1
	    double[] vals = new double[data.numAttributes()];
	    // - age
	    vals[0] = 25;
	    // - sexe (nominal)
	    vals[1] = 0;
	    data.add(new Instance(1,vals));
	    
	    //instance 2
	    // - age
	    vals[0] = 28;
	    // - sexe (nominal)
	    vals[1] = 1;
	    data.add(new Instance(1,vals));
	    System.out.println(data.attribute("sexe").enumerateValues().nextElement());
	    //data.attribute(1).addStringValue("Femme");
	    System.out.println(data.attribute(1));
	}

}
