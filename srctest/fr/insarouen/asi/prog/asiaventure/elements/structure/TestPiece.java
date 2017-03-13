package fr.insarouen.asi.prog.asiaventure.elements.structure;

import fr.insarouen.asi.prog.asiaventure.elements.Entite;
import fr.insarouen.asi.prog.asiaventure.Monde;
import fr.insarouen.asi.prog.asiaventure.elements.structure.ElementStructurel;
import fr.insarouen.asi.prog.asiaventure.elements.objets.Objet;
import fr.insarouen.asi.prog.asiaventure.elements.vivants.Vivant;
import fr.insarouen.asi.prog.asiaventure.NomDEntiteDejaUtiliseDansLeMondeException;
import fr.insarouen.asi.prog.asiaventure.elements.objets.ObjetNonDeplacableException;
import fr.insarouen.asi.prog.asiaventure.elements.structure.ObjetAbsentDeLaPieceException;
import fr.insarouen.asi.prog.asiaventure.elements.structure.VivantAbsentDeLaPieceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.core.IsEqual;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestPiece {
	public Monde monde;
	public Piece piece1;
	public Piece piece2;
	public Objet objet1;
	public Objet objet2;
	public Objet[] objets = new Objet[1];
	public Vivant vivant1;
	public Vivant vivant2;

	@Before
	public void avantTest() throws NomDEntiteDejaUtiliseDansLeMondeException{
		monde = new Monde("monde");
		piece1 = new Piece("test",monde){};
		piece2 = new Piece("test1",monde){};
		objet1 = new Objet("objetInTest",monde){
			public boolean estDeplacable(){
				return true;
			}
		};
		objet2 = new Objet("objetNotInTest",monde){
			public boolean estDeplacable(){
				return false;
			}
		};
		objets[0] = objet1;
		piece1.deposer(objet1);
		piece2.deposer(objet2);
		vivant1 = new Vivant("vivantInTest",monde,500,100,piece1,objet1);
		vivant2 = new Vivant("vivantNotInTest",monde,600,200,piece2,objet2);
	}

	@Test(expected = NomDEntiteDejaUtiliseDansLeMondeException.class)
	public void testConstructeurs() throws NomDEntiteDejaUtiliseDansLeMondeException{
		assertThat(piece1.getNom(), IsEqual.equalTo("test"));
		assertThat(piece1.getMonde().getNom(), IsEqual.equalTo("monde"));
		Piece piece2 = new Piece("test",monde){};
	}
	
	@Test
	public void testContientObjet(){
		piece1.deposer(objet1);
		assertThat(piece1.contientObjet(objet1), IsEqual.equalTo(true));
		assertThat(piece1.contientObjet("objetInTest"), IsEqual.equalTo(true));
		assertThat(piece1.contientObjet(objet2), IsEqual.equalTo(false));
		assertThat(piece1.contientObjet("objetNotInTest"), IsEqual.equalTo(false));
	}

	@Test
	public void testContientVivant(){
		assertThat(piece1.contientVivant(vivant1), IsEqual.equalTo(true));
		assertThat(piece1.contientVivant("vivantInTest"), IsEqual.equalTo(true));
		assertThat(piece1.contientVivant(vivant2), IsEqual.equalTo(false));
		assertThat(piece1.contientVivant("vivantNotInTest"), IsEqual.equalTo(false));
	}

	@Test
	public void testDeposer(){
		piece1.deposer(objet1);
		assertThat(piece1.contientObjet(objet1), IsEqual.equalTo(true));
	}

	@Test
	public void testEntrer(){
		assertThat(piece1.contientVivant(vivant1), IsEqual.equalTo(true));
	}

	@Test
	public void testGetObjets(){
		assertThat(piece1.getObjets(), IsEqual.equalTo(objets));
	}

	@Test(expected = ObjetAbsentDeLaPieceException.class)
	public void testRetirer1() throws ObjetAbsentDeLaPieceException, ObjetNonDeplacableException{
		Objet[] objetsTest = new Objet[0];
		assertThat(piece1.retirer(objet1), IsEqual.equalTo(objet1));
		assertThat(piece1.getObjets(), IsEqual.equalTo(objetsTest));
		piece1.retirer(objet2);
	}

	@Test(expected = ObjetNonDeplacableException.class)
	public void testRetirer2() throws ObjetNonDeplacableException, ObjetAbsentDeLaPieceException{
		Objet[] objetsTest = new Objet[0];
		assertThat(piece1.retirer("objetInTest"), IsEqual.equalTo(objet1));
		assertThat(piece1.getObjets(), IsEqual.equalTo(objetsTest));
		piece2.retirer(objet2);
	}

	@Test(expected = VivantAbsentDeLaPieceException.class)
	public void testSortir() throws VivantAbsentDeLaPieceException{
		Vivant[] vivantsTest = new Vivant[0];
		assertThat(piece1.sortir(vivant1), IsEqual.equalTo(vivant1));
		piece1.entrer(vivant1);
		assertThat(piece1.sortir("vivantInTest"), IsEqual.equalTo(vivant1));
		piece1.sortir(vivant1);
		piece1.sortir(vivant2);
	}
}