/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object for the Polish
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['pl'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, wy\u0142\u0105czone</span>',
		confirmCancel	: 'Pewne opcje zosta\u0142y zmienione. Czy na pewno zamkn\u0105\u0107 okno dialogowe?',
		ok				: 'OK',
		cancel			: 'Anuluj',
		confirmationTitle	: 'Potwierdzenie',
		messageTitle	: 'Informacja',
		inputTitle		: 'Pytanie',
		undo			: 'Cofnij',
		redo			: 'Pon\xF3w',
		skip			: 'Pomi\u0144',
		skipAll			: 'Pomi\u0144 wszystkie',
		makeDecision	: 'Wybierz jedn\u0105 z opcji:',
		rememberDecision: 'Zapami\u0119taj m\xF3j wyb\xF3r'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'pl',
	LangCode : 'pl',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'yyyy-mm-dd HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Foldery',
	FolderLoading	: '\u0141adowanie...',
	FolderNew		: 'Podaj nazw\u0119 nowego folderu: ',
	FolderRename	: 'Podaj now\u0105 nazw\u0119 folderu: ',
	FolderDelete	: 'Czy na pewno chcesz usun\u0105\u0107 folder "%1"?',
	FolderRenaming	: ' (Zmieniam nazw\u0119...)',
	FolderDeleting	: ' (Kasowanie...)',
	DestinationFolder	: 'Folder docelowy',

	// Files
	FileRename		: 'Podaj now\u0105 nazw\u0119 pliku: ',
	FileRenameExt	: 'Czy na pewno chcesz zmieni\u0107 rozszerzenie pliku? Mo\u017Ce to spowodowa\u0107 problemy z otwieraniem pliku przez innych u\u017Cytkownik\xF3w.',
	FileRenaming	: 'Zmieniam nazw\u0119...',
	FileDelete		: 'Czy na pewno chcesz usun\u0105\u0107 plik "%1"?',
	FilesDelete	: 'Czy na pewno chcesz usun\u0105\u0107 pliki (razem: %1)?',
	FilesLoading	: '\u0141adowanie...',
	FilesEmpty		: 'Folder jest pusty',
	DestinationFile	: 'Plik docelowy',
	SkippedFiles	: 'Lista pomini\u0119tych plik\xF3w:',

	// Basket
	BasketFolder		: 'Koszyk',
	BasketClear			: 'Wyczy\u015B\u0107 koszyk',
	BasketRemove		: 'Usu\u0144 z koszyka',
	BasketOpenFolder	: 'Otw\xF3rz folder z plikiem',
	BasketTruncateConfirm : 'Czy naprawd\u0119 chcesz usun\u0105\u0107 wszystkie pliki z koszyka?',
	BasketRemoveConfirm	: 'Czy naprawd\u0119 chcesz usun\u0105\u0107 plik "%1" z koszyka?',
	BasketRemoveConfirmMultiple	: 'Czy naprawd\u0119 chcesz usun\u0105\u0107 pliki (razem: %1) z koszyka?',
	BasketEmpty			: 'Brak plik\xF3w w koszyku. Aby doda\u0107 plik, przeci\u0105gnij i upu\u015B\u0107 (drag\'n\'drop) dowolny plik do koszyka.',
	BasketCopyFilesHere	: 'Skopiuj pliki z koszyka',
	BasketMoveFilesHere	: 'Przenie\u015B pliki z koszyka',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Wy\u015Blij',
	UploadTip	: 'Wy\u015Blij plik',
	Refresh		: 'Od\u015Bwie\u017C',
	Settings	: 'Ustawienia',
	Help		: 'Pomoc',
	HelpTip		: 'Wskaz\xF3wka',

	// Context Menus
	Select			: 'Wybierz',
	SelectThumbnail : 'Wybierz miniaturk\u0119',
	View			: 'Zobacz',
	Download		: 'Pobierz',

	NewSubFolder	: 'Nowy podfolder',
	Rename			: 'Zmie\u0144 nazw\u0119',
	Delete			: 'Usu\u0144',
	DeleteFiles		: 'Usu\u0144 pliki',

	CopyDragDrop	: 'Skopiuj tutaj',
	MoveDragDrop	: 'Przenie\u015B tutaj',

	// Dialogs
	RenameDlgTitle		: 'Zmiana nazwy',
	NewNameDlgTitle		: 'Nowa nazwa',
	FileExistsDlgTitle	: 'Plik ju\u017C istnieje',
	SysErrorDlgTitle : 'B\u0142\u0105d systemu',

	FileOverwrite	: 'Nadpisz',
	FileAutorename	: 'Zmie\u0144 automatycznie nazw\u0119',
	ManuallyRename	: 'Zmie\u0144 nazw\u0119 r\u0119cznie',

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Anuluj',
	CloseBtn	: 'Zamknij',

	// Upload Panel
	UploadTitle			: 'Wy\u015Blij plik',
	UploadSelectLbl		: 'Wybierz plik',
	UploadProgressLbl	: '(Trwa wysy\u0142anie pliku, prosz\u0119 czeka\u0107...)',
	UploadBtn			: 'Wy\u015Blij wybrany plik',
	UploadBtnCancel		: 'Anuluj',

	UploadNoFileMsg		: 'Wybierz plik ze swojego komputera.',
	UploadNoFolder		: 'Wybierz folder przed wys\u0142aniem pliku.',
	UploadNoPerms		: 'Wysy\u0142anie plik\xF3w nie jest dozwolone.',
	UploadUnknError		: 'B\u0142\u0105d podczas wysy\u0142ania pliku.',
	UploadExtIncorrect	: 'Rozszerzenie pliku nie jest dozwolone w tym folderze.',

	// Flash Uploads
	UploadLabel			: 'Pliki do wys\u0142ania',
	UploadTotalFiles	: 'Ilo\u015B\u0107 razem:',
	UploadTotalSize		: 'Rozmiar razem:',
	UploadSend			: 'Wy\u015Blij',
	UploadAddFiles		: 'Dodaj pliki',
	UploadClearFiles	: 'Wyczy\u015B\u0107 wszystko',
	UploadCancel		: 'Anuluj wysy\u0142anie',
	UploadRemove		: 'Usu\u0144',
	UploadRemoveTip		: 'Usu\u0144 !f',
	UploadUploaded		: 'Wys\u0142ano: !n%',
	UploadProcessing	: 'Przetwarzanie...',

	// Settings Panel
	SetTitle		: 'Ustawienia',
	SetView			: 'Widok:',
	SetViewThumb	: 'Miniaturki',
	SetViewList		: 'Lista',
	SetDisplay		: 'Wy\u015Bwietlanie:',
	SetDisplayName	: 'Nazwa pliku',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Rozmiar pliku',
	SetSort			: 'Sortowanie:',
	SetSortName		: 'wg nazwy pliku',
	SetSortDate		: 'wg daty',
	SetSortSize		: 'wg rozmiaru',
	SetSortExtension		: 'wg rozszerzenia',

	// Status Bar
	FilesCountEmpty : '<Pusty folder>',
	FilesCountOne	: '1 plik',
	FilesCountMany	: 'Ilo\u015B\u0107 plik\xF3w: %1',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Wykonanie operacji zako\u0144czy\u0142o si\u0119 niepowodzeniem. (B\u0142\u0105d %1)',
	Errors :
	{
	 10 : 'Nieprawid\u0142owe polecenie (command).',
	 11 : 'Brak wymaganego parametru: typ danych (resource type).',
	 12 : 'Nieprawid\u0142owy typ danych (resource type).',
	102 : 'Nieprawid\u0142owa nazwa pliku lub folderu.',
	103 : 'Wykonanie operacji nie jest mo\u017Cliwe: brak uprawnie\u0144.',
	104 : 'Wykonanie operacji nie powiod\u0142o si\u0119 z powodu niewystarczaj\u0105cych uprawnie\u0144 do systemu plik\xF3w.',
	105 : 'Nieprawid\u0142owe rozszerzenie.',
	109 : 'Nieprawi\u0142owe \u017C\u0105danie.',
	110 : 'Niezidentyfikowany b\u0142\u0105d.',
	111 : 'Wykonanie operacji nie powiod\u0142o si\u0119 z powodu zbyt du\u017Cego rozmiaru pliku wynikowego.',
	115 : 'Plik lub folder o podanej nazwie ju\u017C istnieje.',
	116 : 'Nie znaleziono folderu. Od\u015Bwie\u017C panel i spr\xF3buj ponownie.',
	117 : 'Nie znaleziono pliku. Od\u015Bwie\u017C list\u0119 plik\xF3w i spr\xF3buj ponownie.',
	118 : '\u015Acie\u017Cki \u017Ar\xF3d\u0142owa i docelowa s\u0105 jednakowe.',
	201 : 'Plik o podanej nazwie ju\u017C istnieje. Nazwa przes\u0142anego pliku zosta\u0142a zmieniona na "%1".',
	202 : 'Nieprawid\u0142owy plik.',
	203 : 'Nieprawid\u0142owy plik. Plik przekracza dozwolony rozmiar.',
	204 : 'Przes\u0142any plik jest uszkodzony.',
	205 : 'Brak folderu tymczasowego na serwerze do przesy\u0142ania plik\xF3w.',
	206 : 'Przesy\u0142anie pliku zako\u0144czy\u0142o si\u0119 niepowodzeniem z powod\xF3w bezpiecze\u0144stwa. Plik zawiera dane przypominaj\u0105ce HTML.',
	207 : 'Nazwa przes\u0142anego pliku zosta\u0142a zmieniona na "%1".',
	300 : 'Przenoszenie nie powiod\u0142o si\u0119.',
	301 : 'Kopiowanie nie powiodo si\u0119.',
	500 : 'Mened\u017Cer plik\xF3w jest wy\u0142\u0105czony z powod\xF3w bezpiecze\u0144stwa. Skontaktuj si\u0119 z administratorem oraz sprawd\u017A plik konfiguracyjny CKFindera.',
	501 : 'Tworzenie miniaturek jest wy\u0142\u0105czone.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Nazwa pliku nie mo\u017Ce by\u0107 pusta.',
		FileExists		: 'Plik %s ju\u017C istnieje.',
		FolderEmpty		: 'Nazwa folderu nie mo\u017Ce by\u0107 pusta.',
		FolderExists	: 'Folder %s ju\u017C istnieje.',
		FolderNameExists	: 'Folder ju\u017C istnieje.',

		FileInvChar		: 'Nazwa pliku nie mo\u017Ce zawiera\u0107 \u017Cadnego z podanych znak\xF3w: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Nazwa folderu nie mo\u017Ce zawiera\u0107 \u017Cadnego z podanych znak\xF3w: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Otwarcie pliku w nowym oknie nie powiod\u0142o si\u0119. Nale\u017Cy zmieni\u0107 konfiguracj\u0119 przegl\u0105darki i wy\u0142\u0105czy\u0107 wszelkie blokady okienek popup dla tej strony.',
		XmlError		: 'Nie mo\u017Cna poprawnie za\u0142adowa\u0107 odpowiedzi XML z serwera WWW.',
		XmlEmpty		: 'Nie mo\u017Cna za\u0142adowa\u0107 odpowiedzi XML z serwera WWW. Serwer zwr\xF3ci\u0142 pust\u0105 odpowied\u017A.',
		XmlRawResponse	: 'Odpowied\u017A serwera: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Zmiana rozmiaru %s',
		sizeTooBig		: 'Nie mo\u017Cesz zmieni\u0107 wysoko\u015Bci lub szeroko\u015Bci na warto\u015B\u0107 wi\u0119ksz\u0105 od oryginalnego rozmiaru (%size).',
		resizeSuccess	: 'Obrazek zosta\u0142 pomy\u015Blnie przeskalowany.',
		thumbnailNew	: 'Utw\xF3rz now\u0105 miniaturk\u0119',
		thumbnailSmall	: 'Ma\u0142a (%s)',
		thumbnailMedium	: '\u015Arednia (%s)',
		thumbnailLarge	: 'Du\u017Ca (%s)',
		newSize			: 'Podaj nowe wymiary',
		width			: 'Szeroko\u015B\u0107',
		height			: 'Wysoko\u015B\u0107',
		invalidHeight	: 'Nieprawid\u0142owa wysoko\u015B\u0107.',
		invalidWidth	: 'Nieprawid\u0142owa szeroko\u015B\u0107.',
		invalidName		: 'Nieprawid\u0142owa nazwa pliku.',
		newImage		: 'Utw\xF3rz nowy obrazek',
		noExtensionChange : 'Rozszerzenie pliku nie mo\u017Ce zostac zmienione.',
		imageSmall		: 'Plik \u017Ar\xF3d\u0142owy jest zbyt ma\u0142y.',
		contextMenuName	: 'Zmie\u0144 rozmiar',
		lockRatio		: 'Zablokuj proporcje',
		resetSize		: 'Przywr\xF3\u0107 rozmiar'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Zapisz',
		fileOpenError	: 'Nie uda\u0142o si\u0119 otworzy\u0107 pliku.',
		fileSaveSuccess	: 'Plik zosta\u0142 zapisany pomy\u015Blnie.',
		contextMenuName	: 'Edytuj',
		loadingFile		: 'Trwa \u0142adowanie pliku, prosz\u0119 czeka\u0107...'
	},

	Maximize :
	{
		maximize : 'Maksymalizuj',
		minimize : 'Minimalizuj'
	},

	Gallery :
	{
		current : 'Obrazek {current} z {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Wypakuj tutaj',
		extractToLabel		: 'Wypakuj do...',
		downloadZipLabel	: 'Pobierz jako zip',
		compressZipLabel	: 'Kompresuj do zip',
		removeAndExtract	: 'Usu\u0144 poprzedni i wypakuj',
		extractAndOverwrite	: 'Wypakuj do bie\u017C\u0105cego nadpisuj\u0105c istniej\u0105ce pliki',
		extractSuccess		: 'Plik zosta\u0142 pomy\u015Blnie wypakowany.'
	},

	Search :
	{
		searchPlaceholder : 'Szukaj'
	}
};
