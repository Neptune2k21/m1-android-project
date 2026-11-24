LATEX = xelatex -interaction=nonstopmode -halt-on-error -file-line-error -output-directory=build

.PHONY: all specimen clean

all: build/main.pdf

specimen: build/specimen.pdf

build/%.pdf: %.tex metadonnees.tex preambule.tex $(wildcard style/*.tex) $(wildcard chapitres/*.tex)
	@mkdir -p build
	$(LATEX) $<
	$(LATEX) $<

clean:
	rm -rf build
