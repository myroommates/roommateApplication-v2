package be.flo.roommateapp.vue.technical.navigation;

import android.support.v4.app.Fragment;
import be.flo.roommateapp.R;
import be.flo.roommateapp.vue.fragment.Welcome.WelcomeFragment;
import be.flo.roommateapp.vue.fragment.about.AboutFragment;
import be.flo.roommateapp.vue.fragment.admin.PreferenceFragment;
import be.flo.roommateapp.vue.fragment.admin.RoommateFragment;
import be.flo.roommateapp.vue.fragment.count.ResumeFragment;
import be.flo.roommateapp.vue.fragment.count.TicketListFragment;
import be.flo.roommateapp.vue.fragment.profile.MyProfileFragment;
import be.flo.roommateapp.vue.fragment.shopping.ShoppingItemListFragment;

/**
 * Created by florian on 5/02/15.
 */
public class MenuManager {

    public static enum MenuElement {
        MENU_EL_WELCOME(R.string.nav_drawer_welcome, 0, SubMenuElement.WELCOME),
        MENU_EL_COUNT(R.string.nav_drawer_count, 1, SubMenuElement.COUNT_RESUME, SubMenuElement.COUNT_TICKET_LIST),
        MENU_EL_SHOPPING(R.string.nav_drawer_shopping, 2, SubMenuElement.SHOPPING_LIST),
        MENU_EL_PROFILE(R.string.nav_drawer_my_profile, 3, SubMenuElement.PROFILE_MY_PROFILE),
        MENU_EL_CONFIG(R.string.nav_drawer_config, 4, SubMenuElement.ADMIN_ROOMMATE_LIST, SubMenuElement.ADMIN_PREFERENCE),
        MENU_EL_ABOUT(R.string.nav_drawer_about, 5, SubMenuElement.ABOUT_ABOUT);

        private final int name;
        private final int order;
        private final SubMenuElement[] subMenuElements;

        private MenuElement(int name, int order, SubMenuElement... subMenuElements) {
            this.name = name;
            this.order = order;
            //this.pagerClass = pagerClass;
            this.subMenuElements = subMenuElements;
        }

        public int getName() {
            return name;
        }

        public int getOrder() {
            return order;
        }

        public SubMenuElement[] getSubMenuElements() {
            return subMenuElements;
        }

        public static SubMenuElement getSubMenuElementByPosition(int menuElementOrder, int subMenuElementOrder) {
            for (MenuElement menuElement : MenuElement.values()) {
                if (menuElement.getOrder() == menuElementOrder) {
                    return menuElement.getSubMenuElements()[subMenuElementOrder];
                }
            }
            return null;
        }

        public static MenuElement getByOrder(int position) {
            for (MenuElement menuElement : MenuElement.values()) {
                if (menuElement.getOrder() == position) {
                    return menuElement;
                }
            }

            return null;
        }

        public static MenuElement getByClass(Class<? extends Fragment> aClass) {
            for (MenuElement menuElement : values()) {
                for (SubMenuElement subMenuElement : menuElement.getSubMenuElements()) {
                    if (subMenuElement.fragmentClass.equals(aClass)) {
                        return menuElement;
                    }
                }
            }
            return null;
        }
    }


    public static enum SubMenuElement {
        ADMIN_ROOMMATE_LIST(R.string.nav_config_roommate, RoommateFragment.class),
        ADMIN_PREFERENCE(R.string.nav_config_preference, PreferenceFragment.class),

        COUNT_RESUME(R.string.nav_count_resume, ResumeFragment.class),
        COUNT_TICKET_LIST(R.string.nav_count_ticket, TicketListFragment.class),

        PROFILE_MY_PROFILE(R.string.nav_drawer_my_profile, MyProfileFragment.class),

        SHOPPING_LIST(R.string.nav_shopping_item, ShoppingItemListFragment.class),

        WELCOME(R.string.g_welcome, WelcomeFragment.class),

        ABOUT_ABOUT(R.string.nav_drawer_about, AboutFragment.class);

        public Fragment getFragment() {
            switch (this) {

                case ADMIN_ROOMMATE_LIST:
                    return new RoommateFragment();
                case ADMIN_PREFERENCE:
                    return new PreferenceFragment();
                case COUNT_RESUME:
                    return new ResumeFragment();
                case COUNT_TICKET_LIST:
                    return new TicketListFragment();
                case PROFILE_MY_PROFILE:
                    return new MyProfileFragment();
                case SHOPPING_LIST:
                    return new ShoppingItemListFragment();
                case WELCOME:
                    return new WelcomeFragment();
                case ABOUT_ABOUT:
                    return new AboutFragment();
            }
            return null;
        }


        //private final int order;
        private final int name;
        private final Class<? extends Fragment> fragmentClass;

        SubMenuElement(int name, Class<? extends Fragment> fragmentClass) {
            this.name = name;
            this.fragmentClass = fragmentClass;
        }

        public Integer getOrder() {
            SubMenuElement[] subMenuElements = MenuElement.getByClass(this.fragmentClass).getSubMenuElements();
            for (int i = 0; i < subMenuElements.length; i++) {
                if (subMenuElements[i].equals(this.fragmentClass)) {
                    return i;
                }
            }
            return null;
        }

        public int getName() {
            return name;
        }

        public static SubMenuElement getByClass(Class<?> pagerClass) {
            for (SubMenuElement subMenuElement : values()) {
                if (subMenuElement.fragmentClass.equals(pagerClass)) {
                    return subMenuElement;
                }
            }
            return null;
        }

    }


}
