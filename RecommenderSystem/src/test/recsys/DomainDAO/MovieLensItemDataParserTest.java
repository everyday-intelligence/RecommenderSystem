package test.recsys.DomainDAO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.DomainDAO.MovieLensItemDataParser;

public class MovieLensItemDataParserTest {
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
	public final void testItemMetaData() {
		assertEquals(24, MovieLensItemDataParser.attributesNames.length);
		assertEquals(24, MovieLensItemDataParser.attributesTypes.length);
		//assertEquals(24, MovieLensItemDataParser.isAProperty.length);
		assertEquals(24, MovieLensItemDataParser.isComparable.length);
	}

	@Test
	public final void testParseItemData() {
		List<Item> itemList = new ArrayList<Item>();
		try {
			InputStream ips = new FileInputStream(itemsFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(ligne, "|");
				Item itm = MovieLensItemDataParser.parseItemData(ligne);
				System.out.println(itm);
				itemList.add(itm);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(itemList.size());
		assertEquals(1682, itemList.size());

	}

}
