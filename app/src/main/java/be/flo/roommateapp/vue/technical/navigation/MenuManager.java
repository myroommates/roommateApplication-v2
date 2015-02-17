package be.flo.roommateapp.vue.technical.navigation;

import android.support.v4.app.Fragment;
import be.flo.roommateapp.R;
import be.flo.roommateapp.vue.fragment.Welcome.WelcomeFragment;
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
        MENU_EL_CONFIG(R.string.nav_drawer_config, 3, SubMenuElement.ADMIN_ROOMMATE_LIST),
        MENU_EL_PROFILE(R.string.nav_drawer_my_profile, 4, SubMenuElement.PROFILE_MY_PROFILE);

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
        ADMIN_ROOMMATE_LIST(0, R.string.nav_config_roommate, RoommateFragment.class),

        COUNT_RESUME(0, R.string.nav_count_resume, ResumeFragment.class),
        COUNT_TICKET_LIST(1, R.string.nav_count_ticket, TicketListFragment.class),

        PROFILE_MY_PROFILE(0, R.string.nav_drawer_my_profile, MyProfileFragment.class),

        SHOPPING_LIST(0, R.string.nav_shopping_item, ShoppingItemListFragment.class),

        WELCOME(0, R.string.g_welcome, WelcomeFragment.class);

        public Fragment getFragment() {
            switch (this) {

                case ADMIN_ROOMMATE_LIST:
                    return new RoommateFragment();
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
            }
            return null;
        }


        private final int order;
        private final int name;
        private final Class<? extends Fragment> fragmentClass;

        SubMenuElement(int order, int name, Class<? extends Fragment> fragmentClass) {
            this.order = order;
            this.name = name;
            this.fragmentClass = fragmentClass;
        }

        public int getOrder() {
            return order;
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
