package hardcode.ryga.view.gui;

import hardcode.ryga.controller.S;
import hardcode.ryga.model.domain.Vocable;
import hardcode.ryga.model.domain.VocabularyLookup;
import hardcode.ryga.view.gui.listener.NewVocableListener;
import hardcode.ryga.view.gui.listener.VocableSelectedListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class QueryList extends VBox {

	private VocableSelectedListener vocableSelectedListener;
	private NewVocableListener newVocableListener;
	private VocabularyLookup vocabLookup;

	public void setVocableSelectedListener(
			VocableSelectedListener vocableSelectedListener) {
		this.vocableSelectedListener = vocableSelectedListener;
	}

	public void setNewVocableListener(NewVocableListener newVocableListener) {
		this.newVocableListener = newVocableListener;
	}

	public QueryList(VocabularyLookup vocabLookup) {
		this.vocabLookup = vocabLookup;
		setMinWidth(165);
		setStyle("-fx-background-color: gray");
	}

	public void clear() {
		getChildren().clear();
	}

	public Pane makeElement(Vocable voc) {
		HBox box = new HBox();
		int width = 165;
		int height = 30;
		box.setMinWidth(width);
		box.setPrefWidth(width);
		box.setMaxWidth(width);
		box.setMinHeight(height);
		box.setPrefHeight(height);
		box.setMaxHeight(height);
		box.setPadding(new Insets(5, 5, 5, 5));
		box.setStyle("-fx-background-color: beige; -fx-border-color: black");
		Label l = new Label(voc.getWord());
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		ImageView flag = S.getFlagImageView(voc.getLanguage());
		box.getChildren().addAll(l, spacer, flag);
		box.setOnMouseClicked(e -> {
			vocableSelectedListener.selected(voc);
		});
		return box;
	}

	private List<Pane> old = new ArrayList<>();

	public void setVocables(List<Vocable> vocables) {
		List<Pane> currentElements = new ArrayList<>();
		for (Vocable voc : vocables) {
			Pane p = makeElement(voc);
			currentElements.add(p);
		}
		getChildren().removeAll(old);
		getChildren().addAll(currentElements);
		old = currentElements;

		Pane btnAddPl = makeBtnAddPl();
		getChildren().add(btnAddPl);
		old.add(btnAddPl);
		Pane btnAddDe = makeBtnAddDe();
		getChildren().add(btnAddDe);
		old.add(btnAddDe);
		// TODO: btnAddPl/De muessen nicht staendig entfern werden
	}

	public void query(String query) throws SQLException {
		if (query.isEmpty()) {
			clear();
			return;
		}
		List<Vocable> vocables = vocabLookup.query(query);
		setVocables(vocables);
	}

	private Pane makeBtnAddDe() {
		HBox box = makeBtnAdd("DE");
		return box;
	}

	private Pane makeBtnAddPl() {
		HBox box = makeBtnAdd("PL");
		return box;
	}

	private HBox makeBtnAdd(String language) {
		HBox box = new HBox();
		int width = 165;
		int height = 30;
		box.setMinWidth(width);
		box.setPrefWidth(width);
		box.setMaxWidth(width);
		box.setMinHeight(height);
		box.setPrefHeight(height);
		box.setMaxHeight(height);
		box.setOnMouseClicked(e -> {
			newVocableListener.newVocable(language);
		});
		box.setPadding(new Insets(5, 5, 5, 5));
		box.setSpacing(5);
		box.setStyle("-fx-background-color: yellowgreen; -fx-border-color: black");
		box.getChildren().addAll(S.getPlusImageView(),
				S.getFlagImageView(language));
		return box;
	}

}
