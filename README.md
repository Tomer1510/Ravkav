# Ravkav
* POC project to read &amp; interpret data on the Israeli Ravkav travel card (uses Calypso standard).
* Also include some general-purpose code to brute-force records on compatible smart cards.

# Todo
* Clean up old code in `Main.java` (which was already moved to `Ravkav.java`/`SmartCard.java`.
* Use better design and add documentation.





# Acknowledgments 
* Helps to understand the interpretation process: [http://downloads.pannetrat.com/get/dcada6e83a10a0b4fc73/viva-report.pdf](http://downloads.pannetrat.com/get/dcada6e83a10a0b4fc73/viva-report.pdf).

* Official Calypso specifications: [https://www.calypsonet-asso.org/sites/default/files/010608-NT-CalypsoGenSpecs15.pdf](https://www.calypsonet-asso.org/sites/default/files/010608-NT-CalypsoGenSpecs15.pdf).

* Basic overview of the Calypso specification: [http://secinfo.msi.unilim.fr/lanet/projects/2010-Calypso.pdf](http://secinfo.msi.unilim.fr/lanet/projects/2010-Calypso.pdf).

* Helpful (partial) list for translating station id/operator id/etc into meaningful names: [ravkav-strings](https://github.com/L1L1/cardpeek/blob/master/dot_cardpeek_dir/scripts/etc/ravkav-strings.lua).

* Uses [jaccal](http://sourceforge.net/projects/jaccal/) for communication with the NFC reader.
