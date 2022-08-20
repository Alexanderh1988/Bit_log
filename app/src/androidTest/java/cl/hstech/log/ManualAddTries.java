package cl.hstech.log;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cl.hstech.bitlog.ManualAdd;
import cl.hstech.bitlog.R;

@RunWith(AndroidJUnit4.class)
public class ManualAddTries {

    @Rule
    public ActivityScenarioRule<ManualAdd> ruleTest = new ActivityScenarioRule<>(ManualAdd.class);

    @Test
    public void CompleteParameters (){
        //estoy usando UAI test para verificar que se guardan los parametros
        onView(withId(R.id.item_name)).perform(typeText("Nombre de Item"));
        onView(withId(R.id.editText2)).perform(typeText("Nombre de Area"));
        onView(withId(R.id.editText3)).perform(typeText("Nombre de Categoria2"));
        onView(withId(R.id.editText2)).perform(typeText("Nombre de editText3"));

        //Guardar Datos
        onView(withId(R.id.save_data)).perform(click());
    }

}
