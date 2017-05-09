package Client;

import Answer.Answer;
import PersonalData.Address;
import PersonalData.Person;
import Request.Request;
import Server.PersonManager.ParserType;
import User.User;
import com.sun.istack.internal.NotNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import java.awt.*;
import java.io.IOException;

import static Answer.Comments.OK;
import static User.AccessMask.*;

/**
 * Created by Tarasevich Vladislav on 03.05.2017.
 *
 */
public class GUIHandler
{
    private static final String appName     = "Archive-App";
    private static final String signInText  = "Sign In";
    private static final String DefaultText = "NO DATA";

    private Display display;

    private Shell   signIn;
    private Shell   mainWindow;

    private ClientHandler clientHandler;

    private ParserType[] parsers = new ParserType[]{ParserType.DOM, ParserType.SAX, ParserType.StAX};
    private int currentParser = 0;

    private Answer  answer = null;

    private Composite header = null;
    private Composite body = null;
    private Composite pnlButtons = null;

    private List userViewer;
    private List personViewer;

    private Composite forInformationText;
    private Composite userInformation;

    private Composite forPersonText;
    private Composite personInformation;

    private Text userNameText;
    private Text viewText;
    private Text createText;
    private Text editText;
    private Text deleteText;
    private Text userViewText;
    private Text userEditText;
    private Text userDeleteText;
    private Text changeParserText;

    private Text firstNameText;
    private Text lastNameText;
    private Text emailText;
    private Text phoneNUmberText;
    private Text countryText;
    private Text cityText;
    private Text streetText;
    private Text houseNumberText;
    private Text apartmentNumberText;
    private Text jobPlaceText;
    private Text experienceText;

    public GUIHandler() throws IOException
    {
        clientHandler = new ClientHandler();
    }

    public void showSignIn()
    {
        createSignIn();

        signIn.open();

        while (!signIn.isDisposed())
        {
            if(!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        display.dispose();
        display = null;
    }

    private void createSignIn()
    {
        display = new Display();
        signIn = new Shell(display, SWT.SHELL_TRIM);
        signIn.setLayout(new FormLayout());
        signIn.setSize(320, 180);
        signIn.setModified(false);
        signIn.setText(signInText);
        signIn.addListener(SWT.Close, event -> {
            Toolkit.getDefaultToolkit().beep();
            event.doit = confirmExit(signIn) == SWT.OK;
        });

        //Creating gui
        Label labelLogin = new Label(signIn, SWT.NONE);
        labelLogin.setText("Login : ");

        FormData formData = new FormData(60, 20);
        formData.top  = new FormAttachment(0, 10);
        formData.left = new FormAttachment(0, 10);

        labelLogin.setLayoutData(formData);

        Text login = new Text(signIn, SWT.BORDER);

        formData = new FormData(SWT.DEFAULT, SWT.DEFAULT);
        formData.top    = new FormAttachment(0, 10);
        formData.left   = new FormAttachment(labelLogin, 10);
        formData.right  = new FormAttachment(100, -10);
        formData.height = 16;

        login.setLayoutData(formData);

        Label labelPassword = new Label(signIn, SWT.NONE);
        labelPassword.setText("Password : ");

        formData = new FormData(60, 20);
        formData.top  = new FormAttachment(labelLogin, 10);
        formData.left = new FormAttachment(0, 10);

        labelPassword.setLayoutData(formData);

        Text password = new Text(signIn, SWT.BORDER);
        password.setEchoChar('•');

        formData = new FormData(SWT.DEFAULT, SWT.DEFAULT);
        formData.top    = new FormAttachment(login, 10);
        formData.left   = new FormAttachment(labelPassword, 10);
        formData.right  = new FormAttachment(100, -10);
        formData.height = 16;

        password.setLayoutData(formData);

        Button buttonOk = new Button(signIn, SWT.PUSH);
        buttonOk.setText("Ok");

        formData = new FormData(60, 24);
        formData.right = new FormAttachment(100, -70);
        formData.bottom = new FormAttachment(100, -5);
        buttonOk.setLayoutData(formData);
        buttonOk.addListener(SWT.MouseUp, event -> {
            try
            {
                event.doit = true;
                answer = clientHandler.signIn(login.getText(), password.getText());
                if(answer.Legal() && answer.Success())
                {
                    Toolkit.getDefaultToolkit().beep();
                    signIn.setVisible(false);
                    showMainWindow();
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                    showSignInError(signIn, answer);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        });

        Button buttonSignUp = new Button(signIn, SWT.PUSH);
        buttonSignUp.setText("Sign Up");

        formData = new FormData(60, 24);
        formData.right = new FormAttachment(100, -10);
        formData.top   = new FormAttachment(buttonOk, 0, SWT.TOP);
        buttonSignUp.setLayoutData(formData);
        buttonSignUp.addListener(SWT.MouseUp, event -> {
            try
            {
                Toolkit.getDefaultToolkit().beep();
                answer = clientHandler.signUp(login.getText(), password.getText());
                event.doit =  true;
                if(answer.Legal() && answer.Success())
                {
                    signIn.setVisible(false);
                    showMainWindow();
                }
                else
                {
                    showSignInError(signIn, answer);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    static void showConnectionError()
    {
        Display display = new Display();
        Shell shell = new Shell(display);
        int style = SWT.APPLICATION_MODAL | SWT.CLOSE;
        MessageBox messageBox = new MessageBox(shell, style);
        messageBox.setText("Connection Error");
        messageBox.setMessage("Cannot connect to server.");
        messageBox.open();
    }

    private static void showSignInError(Shell shell, Answer answer)
    {
        int style = SWT.APPLICATION_MODAL | SWT.CLOSE;
        MessageBox messageBox = new MessageBox(shell, style);
        messageBox.setText("Sign In Error");
        switch (answer.getComment())
        {
            case NO_EXIST:
            {
                messageBox.setMessage("Sign In error : Such User does no exist.");
                break;
            }
            case ACCESS_ERROR:
            {
                messageBox.setMessage("Sign In error : invalid access.");
                break;
            }
            case SERVER_ERROR:
            {
                messageBox.setText("Fatal");
                messageBox.setMessage("Server error.");
                break;
            }
            case ALREADY_EXIST:
            {
                messageBox.setText("Sign Up error");
                messageBox.setMessage("Such User already exist.");
                break;
            }
        }
        messageBox.open();
    }

    private void showMainWindow()
    {
        createMainWindow();

        mainWindow.open();

        while (!mainWindow.isDisposed())
        {
            if(!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        mainWindow.dispose();
    }

    private void createMainWindow()
    {
        mainWindow = new Shell(display);
        mainWindow.addListener(SWT.Close, event -> {
            Toolkit.getDefaultToolkit().beep();
            event.doit = confirmExit(mainWindow) == SWT.OK;
            if(event.doit)
            {
                System.exit(0);
            }
        });
        // Менеджер расположения главной формы
        GridLayout gridLayout      = new GridLayout();
        gridLayout.numColumns      = 1;
        gridLayout.marginHeight    = 1;
        gridLayout.marginWidth     = 1;
        gridLayout.verticalSpacing = 2;
        mainWindow.setLayout(gridLayout);

        // Верхняя панель
        createPanelTop(mainWindow);

        // Центральная панель
        createPanelCenter(mainWindow);

        // Панель кнопок управления
        createPanelControls(mainWindow);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createPanelTop(Composite parent)
    {
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 40;

        header = new Composite (parent, SWT.BORDER);
        header.setLayoutData(gridData);
        header.setLayout(new FillLayout());
        new Label(header, SWT.CENTER).setText("User : " + clientHandler.getCurrentUser().getName());

        Button changeParserButton = new Button(header, SWT.PUSH);
        changeParserButton.setText("" + parsers[0]);
        if((clientHandler.getCurrentUser().getAccess()&CHANGE_PARSER) == CHANGE_PARSER)
        {
            changeParserButton.setEnabled(true);
        }
        else changeParserButton.setEnabled(false);
        changeParserButton.addListener(SWT.PUSH, event -> {
            currentParser++;
            currentParser %= 3;
            answer = clientHandler.changeParser(parsers[currentParser]);
            changeParserButton.setText("" + parsers[currentParser]);
        });
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createPanelControls(Composite parent)
    {
        User currentUser = clientHandler.getCurrentUser();
        // Панель кнопок управления
        pnlButtons = new Composite(parent, SWT.BORDER);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 36;
        pnlButtons.setLayoutData(gridData);

        // Размещение кнопок в панели pnlButtons
        pnlButtons.setLayout(new FillLayout());
        Button updatePersonButton = new Button(pnlButtons, SWT.PUSH);
        updatePersonButton.setText("Update Persons");
        if((currentUser.getAccess() & VIEW) == VIEW)
        {
            updatePersonButton.setEnabled(true);
        }
        else updatePersonButton.setEnabled(false);
        updatePersonButton.addListener(SWT.PUSH, event -> {
            Toolkit.getDefaultToolkit().beep();
            answer = clientHandler.getPersons();
            personViewer.removeAll();
            for(Person person : answer.getPersonalData())
            {
                personViewer.add(person.getFirstName() + ' ' + person.getLastName());
            }
        });


        Button addPersonButton = new Button(pnlButtons, SWT.PUSH);
        addPersonButton.setText("Add Person");
        if((currentUser.getAccess()&CREATE) == CREATE)
        {
            addPersonButton.setEnabled(true);
        }
        else addPersonButton.setEnabled(false);
        addPersonButton.addListener(SWT.PUSH, event -> {
            Toolkit.getDefaultToolkit().beep();
            Person person1 = getPersonInformation();
            answer = clientHandler.createPerson(person1);
            personViewer.removeAll();
            for(Person person : answer.getPersonalData())
            {
                personViewer.add(person.getFirstName() + ' ' + person.getLastName());
            }
        });


        Button editPersonButton = new Button(pnlButtons, SWT.PUSH);
        editPersonButton.setText("Edit Person");
        if((currentUser.getAccess()&EDIT) == EDIT)
        {
            editPersonButton.setEnabled(true);
        }
        else editPersonButton.setEnabled(false);
        editPersonButton.addListener(SWT.PUSH, event -> {
            Toolkit.getDefaultToolkit().beep();
            Person oldPerson = clientHandler.getPersonAt(personViewer.getSelectionIndex());
            Person newPerson = getPersonInformation();
            Answer  answer = clientHandler.editPerson(oldPerson, newPerson);
            personViewer.removeAll();
            for(Person person : answer.getPersonalData())
            {
                personViewer.add(person.getFirstName() + ' ' + person.getLastName());
            }
        });

        Button deletePersonButton = new Button(pnlButtons, SWT.PUSH);
        deletePersonButton.setText("Delete Person");
        if((currentUser.getAccess()&DELETE) == DELETE)
        {
            deletePersonButton.setEnabled(true);
        }
        else deletePersonButton.setEnabled(false);
        deletePersonButton.addListener(SWT.PUSH, event -> {
            Toolkit.getDefaultToolkit().beep();
            answer = clientHandler.deletePerson(clientHandler.getPersonAt(personViewer.getSelectionIndex()));
            personViewer.removeAll();
            for(Person person : answer.getPersonalData())
            {
                personViewer.add(person.getFirstName() + ' ' + person.getLastName());
            }
        });


        Button updateUserButton = new Button(pnlButtons, SWT.PUSH);
        updateUserButton.setText("Update Users");
        if((currentUser.getAccess()&USER_VIEW) == USER_VIEW)
        {
            updateUserButton.setEnabled(true);
        }
        else updateUserButton.setEnabled(false);
        updateUserButton.addListener(SWT.PUSH, event -> {
            Toolkit.getDefaultToolkit().beep();
            answer = clientHandler.getUsers();
            userViewer.removeAll();
            for(User user : answer.getUsers())
            {
                userViewer.add(user.getName());
            }
        });


        Button editUserButton  = new Button(pnlButtons, SWT.PUSH);
        editUserButton.setText("Edit User");
        if((currentUser.getAccess()&USER_EDIT) == USER_EDIT)
        {
            editUserButton.setEnabled(true);
        }
        else editUserButton.setEnabled(false);
        editUserButton.addListener(SWT.PUSH, event -> {
            answer = clientHandler.editUser(clientHandler.getUserAt(userViewer.getSelectionIndex()), getUserInformation());
            userViewer.removeAll();
            for(User user : answer.getUsers())
            {
                userViewer.add(user.getName());
            }
        });


        Button deleteUserButton = new Button(pnlButtons, SWT.PUSH);
        deleteUserButton.setText("Delete User");
        if((currentUser.getAccess()&USER_DELETE) == USER_DELETE)
        {
            deleteUserButton.setEnabled(true);
        }
        else deleteUserButton.setEnabled(false);
        deleteUserButton.addListener(SWT.PUSH, event -> {
            answer = clientHandler.deleteUser(clientHandler.getUserAt(userViewer.getSelectionIndex()));
            userViewer.removeAll();
            for(User user : answer.getUsers())
            {
                userViewer.add(user.getName());
            }
        });
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createPanelCenter(Composite parent)
    {
        // Центральная панель
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL |
                GridData.FILL_VERTICAL);
        gridData.grabExcessVerticalSpace = true;
        body = new Composite (parent, SWT.NONE);
        body.setLayoutData(gridData);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns   = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth  = 0;
        body.setLayout(gridLayout);

        SashForm sashForm = new SashForm(body, SWT.NONE);
        sashForm.setOrientation(SWT.HORIZONTAL);
        gridData = new GridData(GridData.FILL_HORIZONTAL |
                GridData.FILL_VERTICAL);
        gridData.horizontalSpan = 3;
        sashForm.setLayoutData(gridData);
        // Формирование интерфейса центральной панели
        createBodyLeft (sashForm);
        createBodyRight(sashForm);
        // Определение положения разделителя
        sashForm.setWeights(new int[] { 2, 5 });
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createBodyLeft(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new GridLayout());


        Composite forInformationLabel = new Composite(composite, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        forInformationLabel.setLayoutData(gridData);
        forInformationLabel.setLayout(new FillLayout());
        new Label(forInformationLabel, SWT.CENTER).setText("Information");

        SashForm sashForm = new SashForm(composite, SWT.NONE);
        sashForm.setOrientation(SWT.VERTICAL);
        gridData = new GridData(GridData.FILL_HORIZONTAL |
                GridData.FILL_VERTICAL);
        gridData.horizontalSpan = 3;
        sashForm.setLayoutData(gridData);

        forInformationText = new Composite(sashForm, SWT.BORDER);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        forInformationText.setLayoutData(gridData1);
        forInformationText.setLayout(new GridLayout());
        userInformation = prepareUserInformation(forInformationText, clientHandler.getCurrentUser());

        forPersonText = new Composite(sashForm, SWT.BORDER);
        gridData1 = new GridData(GridData.FILL_BOTH);
        forPersonText.setLayoutData(gridData1);
        forPersonText.setLayout(new GridLayout());
        personInformation = preparePersonInformation(forPersonText);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createBodyRight(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        composite.setLayoutData(gridData);

        SashForm sashForm2 = new SashForm(composite, SWT.HORIZONTAL);


        Composite comp2_left = new Composite(sashForm2, SWT.BORDER);
        Composite comp2_right = new Composite(sashForm2, SWT.BORDER);

        comp2_left.setLayout(new GridLayout());
        comp2_right.setLayout(new GridLayout());

        Composite forPersonLabel = new Composite(comp2_left, SWT.BORDER);
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        forPersonLabel.setLayoutData(gridData1);
        forPersonLabel.setLayout(new FillLayout());
        new Label(forPersonLabel, SWT.CENTER).setText("Personal Data");

        Composite forPersonList = new Composite(comp2_left, SWT.NONE);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL|GridData.FILL_VERTICAL);
        forPersonList.setLayoutData(gridData2);
        forPersonList.setLayout(new FillLayout());
        personViewer = new List(forPersonList, SWT.FILL);
        personViewer.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                setPersonInformation(clientHandler.getPersonAt(Math.max(personViewer.getSelectionIndex(), 0)));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        Composite forUserLabel = new Composite(comp2_right, SWT.BORDER);
        GridData gridData3 = new GridData(GridData.FILL_HORIZONTAL);
        forUserLabel.setLayoutData(gridData3);
        forUserLabel.setLayout(new FillLayout());
        new Label(forUserLabel, SWT.CENTER).setText("Users");

        Composite forUserList = new Composite(comp2_right, SWT.NONE);
        GridData gridData4 = new GridData(GridData.FILL_BOTH);
        forUserList.setLayoutData(gridData4);
        forUserList.setLayout(new FillLayout());
        userViewer = new List(forUserList, SWT.FILL);
        userViewer.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                setUserInformation(clientHandler.getUserAt(Math.max(userViewer.getSelectionIndex(), 0)));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });
    }

    private int confirmExit(Shell shell)
    {
        int style = SWT.APPLICATION_MODAL | SWT.OK | SWT.CANCEL;
        MessageBox messageBox = new MessageBox(shell, style);
        messageBox.setText("Confirm Exit");
        messageBox.setMessage("Are you sure?");
        return messageBox.open();
    }

    private Composite preparePersonInformation(@NotNull Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        composite.setLayout(new GridLayout());

        personLine(composite, "First name : ", DefaultText);
        personLine(composite, "Last name : ", DefaultText);
        personLine(composite, "Email : ", DefaultText);
        personLine(composite, "Phone number : ", DefaultText);
        personLine(composite, "Country : ", DefaultText);
        personLine(composite, "City : ", DefaultText);
        personLine(composite, "Street : ", DefaultText);
        personLine(composite, "House number : ", DefaultText);
        personLine(composite, "Apartment number : ", DefaultText);
        personLine(composite, "Job place : ", DefaultText);
        personLine(composite, "Experience : ", DefaultText);

        return composite;
    }

    private void personLine(Composite parent, String labelText, String textValue)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        composite.setLayout(new FillLayout());

        new Label(composite, SWT.RIGHT).setText(labelText);

        Text text = new Text(composite, SWT.FILL);
        text.setText(textValue);
        text.setEditable(accessed(clientHandler.getCurrentUser(), EDIT));

        switch (labelText)
        {
            case "First name : ":
            {
                firstNameText = text;
                break;
            }
            case "Last name : ":
            {
                lastNameText = text;
                break;
            }
            case "Email : ":
            {
                emailText = text;
                break;
            }
            case "Phone number : ":
            {
                phoneNUmberText = text;
                break;
            }
            case "City : ":
            {
                cityText = text;
                break;
            }
            case "Country : ":
            {
                countryText = text;
                break;
            }
            case "Street : ":
            {
                streetText = text;
                break;
            }
            case "House number : ":
            {
                houseNumberText = text;
                break;
            }
            case "Apartment number : ":
            {
                apartmentNumberText = text;
                break;
            }
            case "Job place : ":
            {
                jobPlaceText = text;
                break;
            }
            case "Experience : ":
            {
                experienceText = text;
                break;
            }
        }
    }

    private void setPersonInformation(Person person)
    {
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        emailText.setText(person.getEmail());
        phoneNUmberText.setText(person.getPhoneNumber());
        countryText.setText(person.getAddress().getCountry());
        cityText.setText(person.getAddress().getCity());
        streetText.setText(person.getAddress().getStreet());
        houseNumberText.setText(Integer.toString(person.getAddress().getHouseNumber()));
        apartmentNumberText.setText(Integer.toString(person.getAddress().getApartmentNumber()));
        jobPlaceText.setText(person.getJobPlace());
        experienceText.setText(Integer.toString(person.getExperience()));
    }

    private Person getPersonInformation()
    {
        Person  person = new Person();
        Address address = new Address();
        person.setFirstName(firstNameText.getText());
        person.setLastName(lastNameText.getText());
        person.setEmail(emailText.getText());
        person.setPhoneNumber(phoneNUmberText.getText());
        address.setCountry(countryText.getText());
        address.setCity(cityText.getText());
        address.setStreet(streetText.getText());
        address.setHouseNumber(Integer.parseInt(houseNumberText.getText()));
        address.setApartmentNumber(Integer.parseInt(apartmentNumberText.getText()));
        person.setAddress(address);
        person.setJobPlace(jobPlaceText.getText());
        person.setExperience(Integer.parseInt(experienceText.getText()));

        return person;
    }

    private Composite prepareUserInformation(@NotNull Composite parent, @NotNull User user)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        composite.setLayout(new GridLayout());

        //Устанавливаем показ имени пользователя
        Composite userName = new Composite(composite, SWT.NONE);
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        userName.setLayoutData(gridData1);
        userName.setLayout(new FillLayout());

        Label userNameLabel = new Label(userName, SWT.RIGHT);
        userNameLabel.setText("Username : ");

        userNameText = new Text(userName, SWT.FILL);
        userNameText.setText(user.getName());
        userNameText.setEditable(accessed(user, CHANGE_PARSER));

        //Устанавливаем показ доступности просмотра
        userLine(composite, "View : ", user, VIEW);

        //Устанавливаем показ доступности добавления
        userLine(composite, "Creating : ", user, CREATE);

        //Устанавливаем показ доступности редактирования
        userLine(composite, "Editing : ", user, EDIT);

        //Устанавливаем показ доступности удаления
        userLine(composite, "Delete : ", user, DELETE);

        //Устанавливаем показ доступности просмотра пользователей
        userLine(composite, "View users : ", user, USER_VIEW);

        //Устанавливаем показ доступности редактирования пользователей
        userLine(composite, "Edit users : ", user, USER_EDIT);

        //Устанавливаем показ доступности удаления пользователей
        userLine(composite, "Delete users : ", user, USER_DELETE);

        //Устанавливаем показ доступности смены парсера на стороне сервера
        userLine(composite, "Parser change : ", user, CHANGE_PARSER);

        return composite;
    }

    private void setUserInformation(User user)
    {
        userNameText.setText(user.getName());
        viewText.setText(accessedString(user, VIEW));
        createText.setText(accessedString(user, CREATE));
        editText.setText(accessedString(user, EDIT));
        deleteText.setText(accessedString(user, DELETE));
        userViewText.setText(accessedString(user, USER_VIEW));
        userEditText.setText(accessedString(user, USER_EDIT));
        userDeleteText.setText(accessedString(user, USER_DELETE));
        changeParserText.setText(accessedString(user, CHANGE_PARSER));
    }

    private User getUserInformation()
    {
        User user = new User();
        user.setName(userNameText.getText());
        User user2 = clientHandler.getUserAt(userViewer.getSelectionIndex());
        user.setPassword(user2.getPassword());
        byte accessMask = 0;
        accessMask |= viewText.getText().equals("available") ? VIEW : 0;
        accessMask |= createText.getText().equals("available") ? CREATE : 0;
        accessMask |= editText.getText().equals("available") ? EDIT : 0;
        accessMask |= deleteText.getText().equals("available") ? DELETE : 0;
        accessMask |= userViewText.getText().equals("available") ? USER_VIEW : 0;
        accessMask |= userEditText.getText().equals("available") ? USER_EDIT : 0;
        accessMask |= userDeleteText.getText().equals("available") ? USER_DELETE : 0;
        accessMask |= changeParserText.getText().equals("available") ? CHANGE_PARSER : 0;
        user.setAccess(accessMask);

        return user;
    }

    private boolean accessed(User user, byte accessMask)
    {
        return (user.getAccess() & accessMask) == accessMask;
    }

    private String accessedString(User user, byte accessMask)
    {
        return accessed(user, accessMask) ? "available" : "blocked";
    }

    private void userLine(Composite parent, String string, User user, byte accessMask)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        composite.setLayout(new FillLayout());

        new Label(composite, SWT.RIGHT).setText(string);

        Text text = new Text(composite, SWT.FILL);
        text.setText(accessedString(user, accessMask));
        text.setEditable(accessed(user, CHANGE_PARSER));

        switch (accessMask)
        {
            case VIEW:
            {
                viewText = text;
                break;
            }
            case CREATE:
            {
                createText = text;
                break;
            }
            case EDIT:
            {
                editText = text;
                break;
            }
            case DELETE:
            {
                deleteText = text;
                break;
            }
            case USER_VIEW:
            {
                userViewText = text;
                break;
            }
            case USER_EDIT:
            {
                userEditText = text;
                break;
            }
            case USER_DELETE:
            {
                userDeleteText = text;
                break;
            }
            case CHANGE_PARSER:
            {
                changeParserText = text;
                break;
            }
        }
    }
}
